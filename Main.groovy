def log = container.logger
log.info "Inicia el verticle main"

def verticlesFileNames = [
	'VerticleManager.groovy',
	'DeployCommand.groovy'
]

verticlesFileNames.each { vfn ->
	container.deployVerticle(vfn)
}