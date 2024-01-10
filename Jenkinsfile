def CONTAINER_NAME="newsservice"
def CONTAINER_TAG="latest"
def DOCKER_HUB_USER="investasikita"
def DOCKER_REGISTRY="localhost:5000"
def HTTP_PORT="8093"

node {
   
    stage('initialize'){
        def dockerHome = tool 'myDocker'
        def mavenHome  = tool 'myMaven'
        env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
    }
   
    stage('checkout') {
        checkout scm
    }
   
    stage('check java') {
        sh "java -version"
    }

    stage('clean') {
        sh "chmod +x mvnw"
        sh "./mvnw clean"
    }
   
    stage('install tools') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:install-node-and-npm -DnodeVersion=v8.12.0 -DnpmVersion=6.4.1"
    }
   
    stage('npm install') {
        sh "./mvnw com.github.eirslett:frontend-maven-plugin:npm"
    }

    stage('packaging') {
        sh "./mvnw package -Pprod -DskipTests"
        archiveArtifacts artifacts: '**/target/*.war', fingerprint: true
    }
   
    stage('sonar'){
        try {
            sh "mvn sonar:sonar"
        } catch(error){
            echo "The sonar server could not be reached ${error}"
        }
    }
   
    stage('image prune'){
        imagePrune(CONTAINER_NAME)
    }

    stage('image build'){
        imageBuild(CONTAINER_NAME, CONTAINER_TAG)
    }

    stage('push to docker registry'){
        withCredentials([usernamePassword(credentialsId: 'dockerRegistryAccount', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
            pushToImage(CONTAINER_NAME, CONTAINER_TAG, USERNAME, PASSWORD, DOCKER_REGISTRY)
        }
    }

    stage('run app'){
        runApp(CONTAINER_NAME, CONTAINER_TAG, DOCKER_HUB_USER, HTTP_PORT, DOCKER_REGISTRY)
    }

}

def imagePrune(containerName){
    try {
        sh "docker image prune -f"
        sh "docker stop $containerName"
    } catch(error){}
}

def dockerImage
def imageBuild(containerName, tag){
    sh "docker build -t $containerName:$tag  -t $containerName --pull --no-cache ."
    echo "Image build complete"
}

def pushToImage(containerName, tag, dockerUser, dockerPassword, dockerRegistry){
    sh "docker login -u $dockerUser -p $dockerPassword $dockerRegistry"
    sh "docker tag $containerName:$tag $dockerRegistry/$dockerUser/$containerName:$tag"
    sh "docker push $dockerRegistry/$dockerUser/$containerName:$tag"
    echo "Image push complete"
}

def runApp(containerName, tag, dockerHubUser, httpPort, dockerRegistry){
    sh "docker pull $dockerRegistry/$dockerHubUser/$containerName"
    sh "docker run -d --rm -p $httpPort:$httpPort --name $containerName $dockerRegistry/$dockerHubUser/$containerName:$tag"
    echo "Application started on port: ${httpPort} (http)"
}