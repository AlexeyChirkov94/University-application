FROM tomcat:9-jre11
RUN rm -rvf /usr/local/tomcat/webapps/ROOT
ADD  /target/webUniversityApplication.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]
EXPOSE 8080