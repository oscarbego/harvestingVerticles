def log = container.logger
def port = 4433
def sendEchoAddress = 'send-echo'
def eb = vertx.eventBus

log.info "Inicia el verticle manager"

def sendEcho = { msg ->
    eb.send(sendEchoAddress, msg.toString())
}

def registerEchoSender = { sock ->
    eb.registerHandler(sendEchoAddress) { msg ->
        sock.write(msg.body())
    }
}

vertx.createNetServer().connectHandler { sock ->
    registerEchoSender(sock)
    sock.dataHandler { buffer ->
        sendEcho buffer
    }
}.listen(port)