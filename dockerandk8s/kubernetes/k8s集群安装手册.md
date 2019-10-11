# k8s集群安装手册

## 1.系统环境准备

> 所有的服务器都需要满足下面的条件。

### 1.1.服务器要求

> 集群环境最少需要三台服务器，操作系统为 **CentOS_7_2** 及以上版本。

| 服务器配置项 | 要求                    |
| :----------- | ----------------------- |
| 操作系统     | CentOS_7_2_64及以上版本 |
| 内存         | 至少8G                  |
| 存储         | 至少100G                |
| CPU          | 至少1核                 |

### 1.2.关闭防火墙

> 防火墙一定要提前关闭，否则在后续安装K8S集群的时候是个问题制造者。

执行下面语句关闭，并禁用开机启动：

```shell
systemctl stop firewalld & systemctl disable firewalld
```

### 1.3.关闭SeLinux

```shell
setenforce 0
sed -i 's/^SELINUX=enforcing$/SELINUX=disabled/' /etc/selinux/config
```

### 1.4.关闭Swap

> 在安装K8S集群时，Linux的Swap内存交换机制是一定要关闭的，否则会因为内存交换而影响性能以及稳定性。

这里，我们可以提前进行设置：

- 执行`swapoff -a`可临时关闭，但系统重启后恢复
- 编辑`/etc/fstab`，注释掉包含`swap`的那一行即可，重启后可永久关闭，如下所示：

```shell
sed -i '/ swap / s/^/#/' /etc/fstab
```

### 1.5.设置主机名

```shell
#主节点
hostnamectl --static set-hostname  k8s-master
#从节点1
hostnamectl --static set-hostname  k8s-node1
#从节点2
hostnamectl --static set-hostname  k8s-node2
```

### 1.6.修改hosts

```shell
vi /etc/hosts
```

> 每个节点配置相同

```properties
192.168.3.226 k8s-master
192.168.3.225 k8s-node1
192.168.3.228 k8s-node2
```

### 1.7.配置路由参数

> 防止kubeadm报路由警告。
>
> CentOS_7可能会出现iptables被绕过，而导致流量被错误路由的问题。确保 net.bridge.bridge-nf-call-iptables在sysctl配置中设置为1。

将内容写入k8s.conf文件

```shell
cat <<EOF >  /etc/sysctl.d/k8s.conf
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF
```

立即生效

```shell
sysctl -p /etc/sysctl.d/k8s.conf
```

### 1.8.重启系统

```shell
reboot
```

## 2.安装Docker

> 下面提供两种安装方式：存储库安装，rpm安装。rpm主要针对无网的环境下安装。
>
> 所有的服务器都需要安装Docker环境。

### 2.1.存储库安装

> 在首次安装Docker CE之前，需要设置Docker存储库。之后，就可以从存储库安装和更新Docker了。

#### 2.1.1设置存储库

1. 安装需要的包

	```shell
	sudo yum install -y yum-utils  device-mapper-persistent-data  lvm2
	```
	
2.  设置稳定存储库

	```shell
	sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
	```

#### 2.1.2.安装Docker CE

1. 安装最新版

  ```shell
sudo yum install docker-ce
  ```

2. 安装指定版本(可选)

  ```shell
# 查看版本列表
yum list docker-ce --showduplicates | sort -r
# 安装指定版本
sudo yum install docker-ce-<VERSION STRING>
  ```

3. 启动Docker，并设置开机启动

  ```shell
sudo systemctl start docker & systemctl enable docker
  ```

4. 验证是否成功安装

	该命令下载一个测试映像并在容器中运行它。当容器运行时，它打印一条信息消息并退出。

	```shell
	sudo docker run hello-world
	```

5. 查看docker版本

	```shell
	docker --version
	```

####   2.1.3.卸载Docker CE

1. 卸载Docker

	```shell
	sudo yum remove docker-ce
	```

2. 删除自定义配置文件

	```shell
	sudo rm -rf /var/lib/docker
	```

### 2.2.rpm安装

> 访问 <https://download.docker.com/linux/centos/7/x86_64/stable/Packages/> ，下载需要的版本。并上传到服务器。

#### 2.2.1.安装

- 安装时，指定rpm包的路径（安装包在docker文件夹下：**docker-ce-18.09.5-3.el7.x86_64.rpm**）

```shell
sudo yum install /opt/docker/docker-ce-18.09.5-3.el7.x86_64.rpm
```

- 启动Docker

```shell
sudo systemctl start docker & systemctl enable docker
```

- 验证，其他的操作和存储库安装方式一样

```shell
sudo docker run hello-world
```

## 3.安装Docker仓库

> 选择一台服务器作为Docker仓库

### 3.1.企业级私有仓库

> Harbor是由VMware公司开源的企业级的Docker Registry管理项目，它包括权限管理(RBAC)、LDAP、日志审核、管理界面、自我注册、镜像复制和中文支持等功能。Harbo依赖于docker，及docker-compose。

#### 3.1.1.安装docker-compose

> docker-compose网址：https://docs.docker.com/compose/install/

- 在线安装

```shell
# 安装
curl -L https://github.com/docker/compose/releases/download/1.24.0-rc1/docker-compose-`uname -s`-`uname -m` -o /usr/local/bin/docker-compose
# 设置权限
chmod +x /usr/local/bin/docker-compose
```

- 手动安装（安装包在docker文件夹下：**docker-compose-Linux-x86_64**）

	> 下载地址：<https://github.com/docker/compose/releases>

```shell
# 上传docker-compose-Linux-x86_64文件，到/usr/local/bin/docker-compose目录，并修改名称为docker-compose
# 设置权限
chmod +x /usr/local/bin/docker-compose
```

- 查看版本

```shell
docker-compose --version
```

#### 3.1.2.安装Harbor

> Harbor网址：https://github.com/goharbor/harbor/releases

- 安装，下载安装包，上传到服务器，解压：

```shell
tar -vxf  harbor-offline-installer-v1.7.1.tgz
```

- 修改配置，打开解压目录下的`harbor.cfg`文件，修改如下属性，其他的属性根据需要修改。

```properties
# hostname设置访问地址，可以使用ip、域名，不可以设置为127.0.0.1或localhost
hostname = 192.168.3.34
```
- 注意：
	- 默认的端口：80，默认协议：HTTP
	- 如果已经安装了`register`，需要先删除容器
	- 如果使用`HTTP`协议，需要将IP加入注册表

- 启动，运行harbor目录下的`install.sh`

```shell
./install.sh
```

- 登录，直接输入ip即可登录

	http://192.168.3.34

	默认的用户名和密码：admin / Harbor12345

#### 3.1.3.上传镜像

- 首先，需要登录到Harbor仓库

```shell
docker login 192.168.3.34:80
```

- 修改镜像Tag

```shell
docker tag k8s.gcr.io/coredns:1.2.6  192.168.3.34:80/coredns:1.2.6
```

- 上传镜像

```shell
docker push 192.168.3.34:80/coredns:1.2.6
```

- 上传镜像会报如下错误，解决方案参见下面的`3.1.4`

```properties
The push refers to repository [192.168.3.34:80/coredns]
Get https://192.168.3.34:80/v2/: http: server gave HTTP response to HTTPS client
```

#### 3.1.4.加入注册表

> Docker在1.3.x之后默认docker registry使用的是https，会导致私有仓库报错。

有两种方式解决这个问题：

- 搭建HTTPS证书（推荐），该方法操作复杂，本文档的环境不具备条件。

- 加入注册表，修改本地主机的docker启动配置文件，在`/etc/docker/`路径下，添加`daemon.json`文件。

```json
{
  "insecure-registries": [
      "192.168.3.34:80"
   ]
}
```

重启docker

```shell
systemctl restart docker
```

## 4、安装Kubernetes

> 详细的安装信息，参见官网：https://kubernetes.io/docs/setup/independent/install-kubeadm/

### 4.1.存储库安装

#### 4.1.1.设置存储库

```shell
# Google官方镜像源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://packages.cloud.google.com/yum/repos/kubernetes-el7-x86_64
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://packages.cloud.google.com/yum/doc/yum-key.gpg https://packages.cloud.google.com/yum/doc/rpm-package-key.gpg
exclude=kube*
EOF
# 阿里云镜像源
cat <<EOF > /etc/yum.repos.d/kubernetes.repo
[kubernetes]
name=Kubernetes
baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64/
enabled=1
gpgcheck=1
repo_gpgcheck=1
gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
EOF
```

#### 4.1.2.安装kubernetes

1. （所有节点）安装`kubelet`、 `kubeadm`、` kubectl`

```shell
yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
```

2. 启动`kubelet`，并设置开机自启动

```shell
systemctl enable kubelet && systemctl start kubelet
```

3. 配置主节点上的`kubelet`使用`cgroup`驱动程序

```shell
# 查看docker的cgroup驱动
docker info | grep -i cgroup
# 输出结果
Cgroup Driver: cgroupfs
```

-   确保kubelet 的cgroup drive 和docker的cgroup drive一样:

```shell
sed -i "s/cgroup-driver=systemd/cgroup-driver=cgroupfs/g" /etc/systemd/system/kubelet.service.d/10-kubeadm.conf
```

-   重新启动kubelet:

```shell
systemctl daemon-reload
systemctl restart kubelet
```

4. （主节点）初始化master

```shell
kubeadm init --pod-network-cidr=10.244.0.0/16 --kubernetes-version=v1.13.0 --apiserver-advertise-address=192.168.3.30
```

解释：

- `--pod-network-cidr=10.244.0.0/16`表示集群使用`Calico`网络的子网范围。

-  `--kubernetes-version=v1.13.0`表示`K8S`版本，这里必须与导入到Docker镜像版本一致，否则会访问谷歌去重新下载K8S最新版的Docker镜像。

- `--apiserver-advertise-address`表示绑定的主节点的IP。

注意事项
- 若执行`kubeadm init`出错或强制终止，则再需要执行该命令时，需要先执行`kubeadm reset`重置。

**注意，记录下如下信息**

![1546582004846](C:\Users\ZX\AppData\Roaming\Typora\typora-user-images\1546582004846.png)

> 详细说明，参见官网：https://kubernetes.io/docs/setup/independent/create-cluster-kubeadm/

5. 要使kubectl为非root用户工作，请运行以下命令（主节点）

```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

- 如果是`root`用户，则可以运行：

```bash
export KUBECONFIG=/etc/kubernetes/admin.conf
```

6. 安装pod网络附加组件，无网络环境下，需要先下载配置文件

- 网络环境下，执行命令

```shell
kubectl apply -f https://docs.projectcalico.org/v3.3/getting-started/kubernetes/installation/hosted/rbac-kdd.yaml
kubectl apply -f https://docs.projectcalico.org/v3.3/getting-started/kubernetes/installation/hosted/kubernetes-datastore/calico-networking/1.7/calico.yaml
```

- 无网环境下，执行命令

```shell
kubectl apply -f rbac-kdd.yaml
kubectl apply -f calico.yaml
```

- 修改calico.yaml中的配置

```yaml
# Auto-detect the BGP IP address.
- name: IP
  value: "autodetect"
# 添加如下的配置，设置使用的网卡
- name: IP_AUTODETECTION_METHOD
  value: "interface=ens*"
```

- 更新calico

```shell
kubectl apply -f calico.yaml
```

- 查看pod状态

```shell
kubectl get pods --all-namespaces
```

7. 将Master作为工作节点（可选）

- K8S集群默认不会将Pod调度到Master上，这样Master的资源就浪费了。在Master上，可以运行以下命令使其作为一个工作节点：

```shell
kubectl taint nodes --all node-role.kubernetes.io/master-
```

8. 将其他节点加入集群

  - 在其他两个节点上，执行主节点生成的`kubeadm join`命令即可加入集群：

  ```shell
kubeadm join 192.168.3.30:6443 --token rysi00.axpudm4r6vfh08jq --discovery-token-ca-cert-hash sha256:a455ef7bb25b9707098d9b96d4614b63b6246b58fac90a0e3159272e73c59e79
  ```

  - 验证集群是否正常，当所有节点加入集群后，在主节点上运行如下命令，即可看到集群情况

  ```shell
kubectl get nodes
  ```

  - 查看所有pod状态，status全部为Running则表示集群正常。

  ```shell
kubectl get pods -n kube-system
  ```

9.修改apiserver的端口范围（可选）

- 编辑/etc/kubernetes/manifests下的kube-apiserver.yaml文件，在`command`参数下添加如下信息

```yaml
- -–service-node-port-range=1-65535
```

### 4.2.rpm安装

> 访问 <https://pkgs.org/> ，搜索需要下载需要的版本。并上传到服务器。

#### 4.2.1.上传镜像

- （主节点）将images文件夹上传到服务器/opt目录下，运行每个文件夹下的*_load.sh脚本。

```shell
sh /opt/images/calico/calico_load.sh
sh /opt/images/hello/hello_load.sh
sh /opt/images/k8s/k8s_load.sh
sh /opt/images/quay/quay_load.sh
```

#### 4.2.2.安装

- （所有节点）将kubectl文件夹上传到服务器/opt目录下，定位到 /opt/kubectl 的目录，执行

```shell
cd /opt/kubectl
sudo yum -y localinstall *.rpm
```

- 启动，其他操作步骤和存储库安装相同

```shell
systemctl enable kubelet && systemctl start kubelet
```

## 5、安装K8S Dashboard

> 详细信息，参见官网：https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
>
> 在（主节点）安装

### 5.1.安装

-   默认情况下不部署仪表板UI。要部署它，首先从官网获取`kubernetes-dashboard.yaml`，在末尾添加如下配置（主要是设置端口类型为 NodePort）：

	```yaml
	# ------------------- Dashboard Service ------------------- #
	
	kind: Service
	apiVersion: v1
	metadata:
	  labels:
	    k8s-app: kubernetes-dashboard
	  name: kubernetes-dashboard
	  namespace: kube-system
	spec:
	  type: NodePort
	  ports:
	    - port: 443
	      targetPort: 8443
	      nodePort: 30001
	  selector:
	    k8s-app: kubernetes-dashboard
	```

-   运行以下命令：

	```shell
	kubectl create -f kubernetes-dashboard.yaml
	```

### 5.2.查看节点端口

  ```shell
kubectl get service -n kube-system -o wide
  ```

![1547432754970](C:\Users\ZX\AppData\Roaming\Typora\typora-user-images\1547432754970.png)

  记录下端口号，打开页面时需要用到。

### 5.3.创建用户

  - 创建user.yaml文件，内容如下：

  ```yaml
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: kubernetes-dashboard
  name: admin
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin
  namespace: kube-system
  ```

  - 运行命令：

  ```shell
kubectl create -f use.yaml
  ```

### 5.4.获取登录token

  - 获取tokens

```shell
kubectl describe serviceaccount admin -n kube-system
```

  说明：`admin`为创建的user.yaml中的name。

```properties
[root@k8s-master docker]# kubectl describe serviceaccount admin -n kube-system
    Name:                admin
    Namespace:           kube-system
    Labels:              k8s-app=kubernetes-dashboard
    Annotations:         <none>
    Image pull secrets:  <none>
    Mountable secrets:   admin-token-99t6x
    Tokens:              admin-token-99t6x
    Events:              <none>
```

  - tokens信息


```shell
kubectl describe secret admin-token-99t6x  -n kube-system
```

  说明：`admin-token-99t6x`为上述命令查出的`Tokens`内容。

![1546851753513](C:\Users\ZX\AppData\Roaming\Typora\typora-user-images\1546851753513.png)

### 5.5.登录页面

  - 打开连接（**火狐**）： https://192.168.3.30:30001

  - 选择`令牌`登录方式
  - 输入上图中的token，点击登录