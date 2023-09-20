## 项目介绍

灵活聘是基于微信小程序开发的一种在线平台，后端使用SpringBoot、SpringCloud、MySQL、MyBatis等开源搭建而成微服务，项目旨在连接雇主和雇员，提供灵活的短期用工服务。。雇主可通过小程序发布任务，系统会对任务进行审核、收取押金、公布。 雇员可查看任务详情以及雇主信息，或在线报名投递简历、与雇主沟通交流。雇主筛选简历确定合适的人选，系统将生成订单并通知 雇员。雇员完成任务并交付平台，雇主审核结账打分



## 小程序展示

三大主页

![](https://gitee.com/lyzssssss/wechat-mini-program/raw/master/3.jpg)

个人端

![](https://gitee.com/lyzssssss/wechat-mini-program/raw/master/my.jpg)

招聘端

![](https://gitee.com/lyzssssss/wechat-mini-program/raw/master/item.jpg)

聊天端

![](https://gitee.com/lyzssssss/wechat-mini-program/raw/master/chat.jpg)

## 项目使用的技术栈

| 技术名称      | 版本           |
| ------------- | -------------- |
| SpringBoot    | 2.3.9.RELEASE  |
| SpringCloud   | Hoxton.SR10    |
| MyBatis-plus  | 3.4.1          |
| MySQL         | 5.7.25         |
| elasticsearch | 7.12.0         |
| xxl.job       | 2.2.0-SNAPSHOT |
| knife4j       | 2.0.9          |
| JWT           | 0.9.1          |
| minio         |                |
| rabbitmq      |                |
| redis         |                |
| seata         |                |
| sentinel      |                |
| nacos         |                |
| docker        |                |




# 安装Docker

## 1.1.卸载（可选）

如果之前安装过旧版本的Docker，可以使用下面命令卸载：

```
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-selinux \
                  docker-engine-selinux \
                  docker-engine \
                  docker-ce
```



## 1.2.安装docker

首先需要大家虚拟机联网，安装yum工具

```sh
yum install -y yum-utils \
           device-mapper-persistent-data \
           lvm2 --skip-broken
```



然后更新本地镜像源：

```shell
# 设置docker镜像源
yum-config-manager \
    --add-repo \
    https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
    
sed -i 's/download.docker.com/mirrors.aliyun.com\/docker-ce/g' /etc/yum.repos.d/docker-ce.repo

yum makecache fast
```





然后输入命令：

```shell
yum install -y docker-ce
```

docker-ce为社区免费版本。稍等片刻，docker即可安装成功。



 如果报错

```
Error downloading packages:
  docker-engine-17.05.0.ce-1.el7.centos.x86_64: [Errno 256] No more mirrors to try.
```

那么是docker-engine-17.05.0.ce-1.el7.centos.x86_64这个rpm包下载不下来，到http://mirrors.aliyun.com/docker-engine/yum/repo/main/centos/7/Packages/ 仓库看了下，发现阿里云根本就没有这个包。

所以旧智能手动下载，手动安装了

```
wget http://mirrors.aliyun.com/docker-engine/yum/repo/main/centos/7/Packages/docker-engine-17.03.0.ce-1.el7.centos.x86_64.rpm
```



手动安装

```
yum install docker-engine-17.03.0.ce-1.el7.centos.x86_64.rpm 
```



## 1.3.启动docker

Docker应用需要用到各种端口，逐一去修改防火墙设置。非常麻烦，因此建议大家直接关闭防火墙！

启动docker前，一定要关闭防火墙后！！

启动docker前，一定要关闭防火墙后！！

启动docker前，一定要关闭防火墙后！！



```sh
# 关闭
systemctl stop firewalld
# 禁止开机启动防火墙
systemctl disable firewalld
```



通过命令启动docker：

```sh
systemctl start docker  # 启动docker服务

systemctl stop docker  # 停止docker服务

systemctl restart docker  # 重启docker服务
```



 

# 拉取镜像

```
docker pull mysql:5.7.25
docker pull redis
docker pull nacos/nacos-server
docker pull minio/minio
docker pull bladex/sentinel-dashboard
docker pull xuxueli/xxl-job-admin:2.3.0
docker pull seataio/seata-server:1.4.2
docker pull elasticsearch:7.12.1
docker pull kibana:7.12.1
docker pull kibana:7.12.1
```





# 创建容器



## MySQL



```
docker run -p 3306:3306 --name mysql --restart=always --privileged=true \
-v /mydata/mysql/log:/var/log/mysql \
-v /mydata/mysql/data:/var/lib/mysql \
-v /mydata/mysql/conf:/etc/mysql \
-v /etc/localtime:/etc/localtime:ro \
-e MYSQL_ROOT_PASSWORD=lyz20010801 -d mysql:5.7.25
```



## Redis

```
docker run \
-p 6379:6379 \
-v /mydata/redis/data:/data \
-v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf \
--privileged=true \
--name redis \
-d redis:latest redis-server /etc/redis/redis.conf
```



## nacos

```
docker  run \
--name nacos -d \
-p 8848:8848 \
--privileged=true \
--restart=always \
-e JVM_XMS=256m \
-e JVM_XMX=256m \
-e MODE=standalone \
-e PREFER_HOST_MODE=hostname \
-v /mydata/nacos/logs:/home/nacos/logs \
-v /mydata/nacos/init.d/custom.properties:/home/nacos/init.d/custom.properties \
nacos/nacos-server
```



## Minio

```
docker run -p 9090:9000 -p 9001:9001 --name minio \
-v /mydata/minio/data:/data \
-e MINIO_ROOT_USER=minioadmin \
-e MINIO_ROOT_PASSWORD=minioadmin \
-d minio/minio server /data --console-address ":9001"
```

地址：192.168.11.111:9090     账号密码：minioadmin

## sentinel

```
docker run --name sentinel \
-p 8858:8858 \
--privileged=true \
--restart=always \
-d bladex/sentinel-dashboard:latest
```

账号密码：sentinel



## xxl-job

```
docker run -e PARAMS="--spring.datasource.url=jdbc:mysql://jeecg-boot-mysql:3306/xxl_job?Unicode=true&characterEncoding=UTF-8&useSSL=false --spring.datasource.username=root --spring.datasource.password=root --xxl.admin.login=false" ^
-p 9080:8080 --name xxl-job-admin ^
-d xuxueli/xxl-job-admin:2.3.0
```





## seata

```
docker run -d \
--restart always \
--privileged=true \
--name seata-server \
-p 8091:8091 \
-v /mydata/seata:/seata-server \
-e SEATA_IP=192.168.11.111 \
-e SEATA_PORT=8091 \
seataio/seata-server:1.4.2
```

具体看[链接](https://blog.csdn.net/qq_52793248/article/details/128098221)





## es

```
docker network create es-net
```

```
docker run -d \
	--name es \
	-e "http.host=0.0.0.0" \
    -e "ES_JAVA_OPTS=-Xms512m -Xmx512m" \
    -e "discovery.type=single-node" \
    -v /mydata/es/data:/usr/share/elasticsearch/data \
    -v /mydata/es/plugins:/usr/share/elasticsearch/plugins \
    --privileged \
    --network es-net \
    -p 9200:9200 \
    -p 9300:9300 \
elasticsearch:7.12.1
```

将ik及拼音分词挂载到plugins

```
# 4、重启容器
docker restart es
```



## Kibana

```
docker run -d \
--name kibana \
-e ELASTICSEARCH_HOSTS=http://es:9200 \
--network=es-net \
-p 5601:5601  \
kibana:7.12.1
```



## RabbitMQ

```
docker run \
 -e RABBITMQ_DEFAULT_USER=guest \
 -e RABBITMQ_DEFAULT_PASS=guest \
 --name mq \
 --hostname mq1 \
 -p 15672:15672 \
 -p 5672:5672 \
 -d \
 rabbitmq:management
```






喜欢的给个星支持一下
