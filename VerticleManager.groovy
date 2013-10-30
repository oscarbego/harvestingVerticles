def log = container.logger
log.info "Inicia el verticle manager"

def port = 4433
vertx.createNetServer().connectHandler { sock ->
    sock.dataHandler { buffer ->
        sock.write(buffer)
    }
}.listen(port)