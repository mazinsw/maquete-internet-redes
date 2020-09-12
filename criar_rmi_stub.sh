#!/bin/sh

cd Classes/bin/
rmic host.HostImpl
rmic router.RouterImpl
rmic server.ServerImpl
cd ../../Servidor/bin/
rmic host.HostImpl
rmic router.RouterImpl
rmic server.ServerImpl
cd ../../Roteador/bin/
rmic host.HostImpl
rmic router.RouterImpl
rmic server.ServerImpl
cd ../../Host/bin/
rmic host.HostImpl
rmic router.RouterImpl
rmic server.ServerImpl

