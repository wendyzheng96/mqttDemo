
# mqttDemo  
 Android使用Mqtt协议链接ActiveMQ服务器实现推送  
  ## MQTT 特点  
  
MQTT 协议是为大量计算能力有限，且工作在低带宽、不可靠的网络的远程传感器和控制设备通讯而设计的协议，它具有以下主要的几项特性：  
  
1. 使用发布/订阅消息模式，提供一对多的消息发布，解除应用程序耦合。   
2. 对负载内容屏蔽的消息传输。  
  
3. 使用 TCP/IP 提供网络连接。    
主流的 MQTT 是基于 TCP 连接进行数据推送的，但是同样有基于 UDP 的版本，叫做 MQTT-SN 。这两种版本由于基于不同的连接方式，优缺点自然也就各有不同了。  
  
4. 有三种消息发布服务质量：  
  
> - “至多一次”，消息发布完全依赖底层 TCP/IP 网络。会发生消息丢失或重复。这一种方式主要普通 APP 的推送，倘若你的智能设备在消息推送时未联网，推送过去没收到，再次联网也就收不到了。  
>   
> - “至少一次”，确保消息到达，但消息重复可能会发生。    
>   
> - “只有一次”，确保消息到达一次。这种最高质量的消息发布服务还可以用于即时通讯类的 APP 的推送，确保用户收到且只会收到一次。  
  
5. 小型传输，开销很小（固定长度的头部是2字节），协议交换最小化，以降低网络流量。  
  
6. 使用 Last Will 和 Testament 特性通知有关各方客户端异常中断的机制。  
  
>- Last Will：即遗言机制，用于通知同一主题下的其他设备发送遗言的设备已经断开了连接。  
>  
>- Testament：遗嘱机制，功能类似于 Last Will 。  
  
## ActiveMQ  
  
>ActiveMQ  是一个 MOM（Message Orient middleware，面向消息的中间件），具体来说是一个实现了 JMS（Java Message Service，Java消息服务）  规范的系统间远程通信的消息代理。  
  
### 特点：  
  
1. 支持多种语言编写客户端 ；  
  
2. 对spring的支持，很容易和spring整合 ；  
  
3. 支持多种传输协议：TCP,SSL,NIO,UDP等 ；  
  
4. 支持AJAX.  
  
  
### 消息形式：  
  
- 点对点（queue）  
  
- 一对多（topic）  
  
## 安装和配置  
  
1. [下载ActiveMQ](http://activemq.apache.org/download.html)
下载之后解压，如下：
![apache-activemq-5.15.8解压后](https://github.com/wendyzheng96/mqttDemo/blob/master/image/process_1.png?raw=true)

2. 查看端口
打开activeMq的conf文件夹中的activemq.xml，查看服务器的端口，mqtt默认端口号是1883，当然你也可以修改。
![enter image description here](https://github.com/wendyzheng96/mqttDemo/blob/master/image/process_5.png?raw=true)

3. 启动服务
进入 G:\apache-activemq-5.15.8\bin\win64 文件夹，双击 activemq.bat 文件，
![enter image description here](https://github.com/wendyzheng96/mqttDemo/blob/master/image/process_2.png?raw=true)
会自动启动命令行窗口，
![enter image description here](https://github.com/wendyzheng96/mqttDemo/blob/master/image/process_3.png?raw=true)
之后直接在浏览器访问 [http://localhost:8161](http://localhost:8161)，出现如下图所示页面，则说明配置成功。
![enter image description here](https://github.com/wendyzheng96/mqttDemo/blob/master/image/process_4.png?raw=true)


