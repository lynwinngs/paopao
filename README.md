# netty  第一课提交作业 学员GP130222：
 micro-Chat/micro-Chat
 时间比较紧，只完成基本架构，功能未全部完成。
 一、
 1、server主类：ChatServer
 2、client主类：ClientMian
 二、服务端：
  1、ChatServer，接收到客户端请求后分发给子线程处理
  * 接收客户端登录请求，返回在线用户列表。
  * 定时推送在线用户列表给所有客户端
  * 使用线程池处理
  2、ChatHandler
  处理策略类，负责分发服务
  3、ChatLogin
   * 登录：
   * 获取发送者IP和端口，保存；
   * 给发送者返回登录成功报文。
   4、ChatLogout
  * 登出：
  * 获取发送者QQ号，移出在线列表。
  5、ChatTransmit
   * 获取接收者IP和端口号；
   * 如果是推送消息建立新socket，如果是登录返回，使用原socket
   * 发送报文。
   三、客户端：
   1、ClientMian
    * 客户端主类
    * 启动客户端接收线程和发送线程；
    * 使用线程池处理
   2、ChatClient
    * 客户端-发送，收到请求后分发给子线程处理
    * 1、登录，向服务端发送等登录请求，根据返回报文更新在线用户列表
    * 2、根据好友QQ号，发送消息。向另外一个客户端建立了Socket连接以后，保存该Socket，此次会话后续
    * 都使用该Socket向此好友发送消息。直到我方发送“BYE”消息（命令）。
    * 3、获取本地存储的在线用户列表。（还没做好）
    * 4、登出。(还没做好。)
   3、ChatClientAccept
    * 客户端-接收，接收后分发给子线程处理,使用随机端口号
    * 除客户端给服务器发送请求的返回报文以外，所有报文由该线程接收。
    * 1、接收服务器推送的所有在线用户列表，更新本地存储。
    * 2、接收消息，根据不同QQ号转发给不同的Socket处理线程处理，
    *      如果该Socket处理线程还不存在，则新建一个，把报文转发给该线程，等待处理。
    *      如果已经存在则把收到的消息放入到该线程的阻塞队列中，等待读取。
   4、ChatBox
    消息处理类接口

