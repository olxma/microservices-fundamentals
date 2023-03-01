package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return binary file when resource exists"
    request {
        method GET()
        url("/resources/1")
    }
    response {
        body(fileAsBytes("test.mp3"))
        headers {
            contentType("audio/mpeg")
        }
        status 200
    }
}
