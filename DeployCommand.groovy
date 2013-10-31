import groovy.io.FileType

def log = container.logger
def eb = vertx.eventBus

log.info "Inicia el verticle deploy command"

def sendEcho = { msg ->
    eb.send('send-echo', msg.toString())
}

def getVerticlesFilesNames = {
	def verticlesFilesNames = []
	new File(".").eachFileMatch(FileType.FILES, ~/^.*groovy$/) { file ->
	    verticlesFilesNames << file.name
	}
	return verticlesFilesNames
}

def handler = { msg ->
	def command = msg.body().command
	def arguments = msg.body().arguments
	
	if(command == 'deploy') {
		def verticlesFound = []
		arguments.each { ar ->
			verticlesFound << getVerticlesFilesNames().findAll { vf ->
				vf.toLowerCase().startsWith(ar)
			}
		}
		verticlesFound.flatten().each { vn ->
			container.deployVerticle(vn) { asyncResult ->
                if(asyncResult.succeeded()) {
                    sendEcho "[DEPLOY:OK] ${vn} :: ${asyncResult.result()}"
                } else {
                    sendEcho "[DEPLOY:ERROR] ${vn} :: ${asyncResult.cause()}"
                }
            }
		}
	}
}

eb.registerHandler('execute-command', handler) {
	log.info "Comando [deploy [..]] disponible..."
}