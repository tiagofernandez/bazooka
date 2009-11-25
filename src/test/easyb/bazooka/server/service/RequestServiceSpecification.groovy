package bazooka.server.service

import bazooka.common.exception.ExistingRequestException
import bazooka.common.exception.NonExistingRequestException
import bazooka.common.model.Request
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(RequestServiceImpl.class)

it "should save a new request", {
  service.saveRequest(new Request("My Request")).id.shouldNotBe null
}

it "should not save requests with the same name", {
  name = "Another Request"
  service.saveRequest(new Request(name))

  ensureThrows(ExistingRequestException) {
    service.saveRequest(new Request(name))
	}
}

it "should update a payload for an existing request", {
  request = service.saveRequest(new Request("Abc", "<foo/>"))

  newPayload = "<bar/>"
  request.payload = newPayload

  service.updateRequest(request).payload.shouldBe newPayload
}

it "should not save a payload for a non-existing request", {
  ensureThrows(NonExistingRequestException) {
    service.updateRequest(new Request("Null", "{ \"foo\": { \"bar\": \"baz\" } }"))
  }
}

it "should save a request and get its payload", {
  name = "Yet Another Request"
  payload = "<foo><bar>baz</bar></foo>"

  service.saveRequest(new Request(name, payload))
  service.getRequest(name).payload.shouldBe payload
}

it "should delete an existing request", {
  name = "Some Request"
  request = service.saveRequest(new Request(name))
  service.deleteRequest(request)

  ensureThrows(NonExistingRequestException) {
    service.getRequest(name)
	}
}

it "should not delete a request that does not exist", {
  ensureThrows(NonExistingRequestException) {
    service.deleteRequest(new Request("NonExisting"))
	}
}

it "should list the existing requests", {
  service.saveRequest(new Request("Foo"))
  service.saveRequest(new Request("Bar"))
  service.listRequests().size().shouldBeGreaterThan 2
}