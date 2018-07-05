## NBServerDemo

完成了与电信物联网平台的对接，整个工程包括四个组件，分别是Mina服务端，Apache客户端，消息队列，Mqtt客户端。Mina服务端实现了SSL双向认证，消息队列用于临时缓存消息，Mqtt客户端实现消息从http到mqtt的转发。