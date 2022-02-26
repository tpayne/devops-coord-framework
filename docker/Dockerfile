# This is a docker make file for building this Maven app
# and putting it into a docker image...

FROM maven:3.8-jdk-11 as imagebuilder
RUN apt-get update

WORKDIR /app

COPY pom.xml .
COPY devops-framework-core ./devops-framework-core
COPY devops-framework-pipeline ./devops-framework-pipeline
COPY devops-framework-plugin ./devops-framework-plugin

# Comment out security scan unless really needed, it takes too long to run
# RUN mvn -q clean install -Psecurity-scans -Denv.DEVOPS_FRAMEWORK_UNITTESTS=true -Dmaven.test.skip=true
RUN mvn -q -B clean package -Denv.DEVOPS_FRAMEWORK_UNITTESTS=true -Dmaven.test.skip=true

# Jenkins image which we will modify...
FROM jenkins/jenkins:lts as jenkins
RUN apt-get update 

ARG HPI_FILE=/app/devops-framework-plugin/target/devops-framework-plugin.hpi
WORKDIR /var/jenkins_home/plugins/

#RUN chown -R jenkins:jenkins /var/jenkins_home/plugins/
#RUN chmod -R o+rwx /var/jenkins_home/plugins/

COPY --from=imagebuilder ${HPI_FILE} /var/jenkins_home/devops-framework-plugin.hpi
COPY scripts/jenkins.sh /usr/local/bin/jenkins.sh
COPY scripts/install-plugins.sh /usr/local/bin/install-plugins.sh

VOLUME /var/jenkins_home/

CMD ["/bin/bash","-e","/usr/local/bin/jenkins.sh"]

EXPOSE 8080
