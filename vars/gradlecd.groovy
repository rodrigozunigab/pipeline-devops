def call(){
  
        def downloadOK = false;
        figlet 'Gradle'
        figlet 'Despliegue Continuo'

        stage("downloadNexus"){    
            env.TAREA =  env.STAGE_NAME       
            sh 'curl -X GET -u admin:admin http://localhost:8081/repository/test-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar -O' 
            downloadOK = true;
        }


        stage("runDownloadedJar"){    
            env.TAREA =  env.STAGE_NAME   
            if (downloadOK) {
                sh "java -jar DevOpsUsach2020-0.0.1.jar &"
                sleep 20   
            }             
        }  

        stage("rest"){
            env.TAREA =  env.STAGE_NAME 
            if (downloadOK) 
                sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
        }  

        stage("nexusCD"){    
            env.TAREA =  env.STAGE_NAME   
            if (downloadOK)          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: 'release-v1.0.0']]]                     
        }                    

}

return this;