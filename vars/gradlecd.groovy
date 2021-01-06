def call(){
  
        def buildEjecutado = false;
        echo 'Gradle'
        echo 'Despliegue Continuo'

        stage("downloadNexus"){    
            env.TAREA =  env.STAGE_NAME   
            if (buildEjecutado)          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '2.0.1']]]                     
        }  

        stage("runDownloadedJar"){    
            env.TAREA =  env.STAGE_NAME   
            if (buildEjecutado)          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '2.0.1']]]                     
        }  

        stage("rest"){
            env.TAREA =  env.STAGE_NAME 
            if (buildEjecutado) 
                sh 'curl -X GET "http://localhost:8081/rest/mscovid/test?msg=testing"'
        }  

        stage("nexusCD"){    
            env.TAREA =  env.STAGE_NAME   
            if (buildEjecutado)          
                nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar']], mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '2.0.1']]]                     
        }                    

}

return this;