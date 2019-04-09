#
# Makefile to create docker images
#
# docker ps -a
# docker logs <containerId>
# kubectl logs <podName>
# kubectl run debug-container -it --image=<imageName>
# kubectl get pods
# kubectl describe pod/<podName>
# kubectl get -a pods --watch

all: buildDev buildStd
	@echo Built image

#Replace the APPNAME with your app...
APPNAMEDEV=server/linux-dev
APPNAMESTD=server/linux-std
APPBUILDTAG="0.1"

#Replace the REGISTRY used to reflect your system...
REGISTRY=localhost:5000
REGIMGNAMEDEV=$(REGISTRY)/$(APPNAMEDEV)
REGIMGNAMESTD=$(REGISTRY)/$(APPNAMESTD)

IMGNAMEDEV=$(APPNAMEDEV):$(APPBUILDTAG)
IMGNAMESTD=$(APPNAMESTD):$(APPBUILDTAG)

buildDev:
	@echo - Creating dev image - this will take a while...
	@docker build -q -f Dockerfile.linux-dev . -t $(IMGNAMEDEV)

buildStd:
	@echo - Creating std image - this will take a while...
	@docker build -q -f Dockerfile.linux-base . -t $(IMGNAMESTD)

push: buildDev buildStd
	@echo - Pushing images - this will take a while...
	@-docker run -d -p 5000:5000 --restart=always --name registry registry:2
	@docker tag $(IMGNAMEDEV) $(REGISTRY)/$(IMGNAMEDEV)
	@docker tag $(IMGNAMESTD) $(REGISTRY)/$(IMGNAMESTD)
	@docker push $(REGIMGNAMEDEV)
	@docker push $(REGIMGNAMESTD)

pods: push
	@echo - Creating pods - this will take a while...
	@kubectl create -f linux-dev-pod.yml
	@kubectl create -f linux-std-pod.yml

clean:
	@echo - Running clean - this will take a while...
	@-docker rmi -f $(IMGNAMEDEV) $(IMGNAMESTD) registry:2
	@-kubectl delete pod/linux-dev
	@-kubectl delete pod/linux-std

cleanall:
	@echo - Deleting ALL docker images - very dangerous... 
	@-docker ps | gawk '{ printf("\ndocker rm -f %s",$$1);}' | sh -xv
	@-docker images | gawk '{ printf("\ndocker rmi -f %s",$$3);}' | sh -xv
	@-docker container prune -f 

test: buildDev buildStd
	@echo - Running tests - this will take a while...
	docker run --rm -it $(IMGNAMEDEV) /bin/sh -c "yum list -y"
	docker run --rm -it $(IMGNAMESTD) /bin/sh -c "yum list -y"