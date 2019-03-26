#
# Makefile to create docker images
#

all: buildDev buildStd
	@echo Built image

#Replace the APPNAME with your app...
APPNAMEDEV=server/linux-dev
APPNAMESTD=server/linux-std
APPBUILDTAG="0.1"

#Replace the REGISTRY used to reflect your system...
REGISTRY=localhost:5000
REGIMGNAMEDEV=$(REGISTRY)/$(APPNAMEDEV):$(APPBUILDTAG)
REGIMGNAMESTD=$(REGISTRY)/$(APPNAMESTD):$(APPBUILDTAG)

IMGNAMEDEV=$(APPNAMEDEV):$(APPBUILDTAG)
IMGNAMESTD=$(APPNAMESTD):$(APPBUILDTAG)

buildDev:
	@echo Creating image...
	docker build -f Dockerfile.linux-dev . -t $(IMGNAMEDEV)

buildStd:
	@echo Creating image...
	docker build -f Dockerfile.linux-base . -t $(IMGNAMESTD)

push: buildDev buildStd
	-docker run -d -p 5000:5000 --restart=always --name registry registry:2
	docker tag $(IMGNAMEDEV) $(REGISTRY)/$(IMGNAMEDEV)
	docker tag $(IMGNAMESTD) $(REGISTRY)/$(IMGNAMESTD)
	docker push $(REGIMGNAMEDEV)
	docker push $(REGIMGNAMESTD)

pods: push
	kubectl create -f linux-dev-pod.yml
	kubectl create -f linux-std-pod.yml

clean:
	-docker rmi -f $(IMGNAMEDEV) $(IMGNAMESTD)
	-kubectl delete pod/linux-dev
	-kubectl delete pod/linux-std

test: buildDev buildStd
	docker run --rm -it $(IMGNAMEDEV) /bin/sh -c "yum list -y"
	docker run --rm -it $(IMGNAMESTD) /bin/sh -c "yum list -y"