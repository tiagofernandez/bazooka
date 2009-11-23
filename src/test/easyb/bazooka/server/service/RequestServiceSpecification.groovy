package bazooka.server.service

import bazooka.common.exception.ExistingRequestException
import bazooka.common.exception.NonExistingRequestException
import bazooka.server.context.BazookaContext
import bazooka.server.persistence.PersistenceModule

service = new BazookaContext(new PersistenceModule("bazooka-test")).injector.getInstance(RequestServiceImpl.class)

it "should save a new request", {
  service.saveRequest("My Request", "")
}

it "should not save requests with the same name", {
  request = "Some Request"
  service.saveRequest(request, "")

  ensureThrows(ExistingRequestException) {
    service.saveRequest(request, "")
	}
}

it "should not save a payload for a non-existing request", {
  ensureThrows(NonExistingRequestException) {
    service.updateRequest("Void", "{ \"foo\":  { \"bar\": \"baz\" } }")
  }
}

it "should save a request and get its payload", {
  request = "Yet Another Request"
  payload = "<foo><bar>baz</bar></foo>"

  service.saveRequest(request, payload)
  service.getPayload(request).shouldBe payload
}

it "should delete an existing request", {
  request = "Anything"
  service.saveRequest(request, "")
  service.deleteRequest(request)
}

it "should not delete a request that does not exist", {
  ensureThrows(NonExistingRequestException) {
    service.deleteRequest("NonExisting")
	}
}

it "should list the existing requests", {
  service.saveRequest("Foo", "")
  service.saveRequest("Bar", "")
  service.listRequests().size().shouldBeGreaterThan 2
}