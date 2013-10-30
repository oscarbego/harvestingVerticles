def log = container.logger
def eb = vertx.eventBus

log.info "Inicia el verticle deploy command"

def sendEcho = { msg ->
    eb.send('send-echo', msg.toString())
}

def handler = { msg ->
	def command = msg.body().command
	def arguments = msg.body().arguments
	
	if(command == 'deploy') {
		sendEcho "deploy ${arguments}"
	}
}

eb.registerHandler('execute-command', handler) {
	log.info "Comando [deploy [..]] disponible..."
}