package bazooka.server.service

import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(RequestServiceImpl.class)

it "should save a new request"

it "should not save requests with the same name"

it "should delete an existing request"

it "should list the existing requests"