def log = container.logger
def eb = vertx.eventBus

log.info "Inicia el verticle execute command"

eb.registerHandler('search-command') { msg ->
	def inputList = msg.body().toString().split()
    if (!inputList) return null

    def command = inputList.head()
    def arguments = inputList.tail()*.toLowerCase()

    eb.publish('execute-command', [
        command: command, 
        arguments: arguments
    ])
}