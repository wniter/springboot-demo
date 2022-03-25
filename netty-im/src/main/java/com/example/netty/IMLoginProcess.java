package com.example.netty;
/**
 * 单体IM系统中，首先需要登录。登录的流程，从端到端（End to End）的角度来说，
 * 包括以下环节：
 * （1）客户端发送登录数据包。
 * （2）服务器端进行用户信息验证。
 * （3）服务器端创建Session会话。
 * （4）服务器端返回登录结果的信息给客户端，包括成功标志、Session ID等
 *  整个端到端（End to End）的登录流程中，涉及到4次编/解码：
 * （1）客户端编码：客户端对登录请求的Protobuf数据包进行编码。
 * （2）服务器端解码：服务端对登录请求的Protobuf数据包进行解码。
 * （3）服务器端编码：服务端对编码登录响应的Protobuf数据包进行编码。
 * （4）客户端解码：客户端对登录响应的Protobuf数据包进行解码。
 */
/**
 * 图解登录/响应流程的9个环节
 * 从客户端到服务器端再到客户端，9个环节的介绍如下：
 * （1）首先，客户端收集用户ID和密码，这一步需要使用到LoginConsoleCommand控制
 * 台命令类。
 * （2）然后，客户端发送Protobuf数据包到客户端通道，这一步需要通过LoginSender发
 * 送器组装Protobuf数据包。
 * （3）客户端通道将Protobuf数据包发送到对端，这一步需要通过Netty底层来完成。
 * （4）服务器子通道收到Protobuf数据包，这一步需要通过Netty底层来完成。
 * （5）服务端UserLoginHandler入站处理器收到登录消息，交给业务处理器
 * LoginMsgProcesser处理异步的业务逻辑。
 * （6）服务端LoginMsgProcesser处理完异步的业务逻辑，就将处理结果写入用户绑定的
 * 子通道。
 * （7）服务器子通道将登录响应Protobuf数据帧发送到客户端，这一步需要通过Netty底
 * 层来完成。
 * （8）客户端通道收到Protobuf登录响应数据包，这一步需要通过Netty底层来完成。
 * （9）客户端LoginResponceHandler业务处理器处理登录响应，例如设置登录的状态，
 * 保存会话的Session ID等等。
 */
/**
 * 客户端涉及的主要模块
 * 在IM登录的整体执行流程中，客户端所涉及的主要模块大致如下：
 * （1）ClientCommand模块：控制台命令收集器。
 * （2）ProtobufBuilder模块：Protobuf数据包构造者.
 * （3）Sender模块：数据包发送器。
 * （4）Handler模块：服务器响应处理器。
 * 上面的这些模块都有一个或者多个专门的POJO Java类来完成对应的工作：
 * （1）LoginConsoleCommand类：属于ClientCommand模块，它负责收集用户在控制台
 * 输入的用户ID和密码。
 * （2）CommandController类：属于ClientCommand模块，它负责收集用户在控制台输入
 * 的命令类型，根据相应的类型调用相应的命令处理器，然后收集相应的信息。例如，如果
 * 用户输入的命令类型为登录，则调用LoginConsoleCommand命令处理器，将收集到的用户
 * ID和密码封装成User类，然后启动登录处理。
 * （3）LoginMsgBuilder类：属于ProtobufBuilder模块，它负责将User类组装成Protobuf
 * 登录请求数据包。
 * （4）LoginSender类：属于Sender模块，它负责将组装好Protobuf登录数据包发送到服
 * 务器端。
 * （5）LoginResponceHandler类：属于Handler模块，它负责处理服务器端的登录响应。
 */
/**
 * 服务器端涉及的主要模块
 * 在IM登录的整体执行流程中，服务器端所涉及的主要模块如下：
 * （1）Handler模块：客户端请求的处理。
 * （2）Processer模块：以异步方式完成请求的业务逻辑处理。
 * （3）Session模块：管理用户与通道的绑定关系。
 * 在具体的服务器登录流程中，上面的这些模块都有一个或者多个专门的Java类来完成
 * 对应的工作，大致的类为：
 * （1）UserLoginRequestHandler类：属于Handler模块，负责处理收到的Protobuf登录请
 * 求包，然后使用LoginProcesser类，以异步方式进行用户校验。
 * （2）LoginProcesser类：属于Processer模块，完成服务器端的用户校验，再将校验的
 * 结果组装成一个登录响应Protobuf数据包写回到客户端。
 * （3）ServerSession类：属于Session模块，如果校验成功，设置相应的会话状态；然
 * 后，将会话加入到服务器端的SessionMap映射中，这样该用户就可以接受其他用户发送的
 * 聊天消息。
 */
//public class IMLoginProcess {
//}
