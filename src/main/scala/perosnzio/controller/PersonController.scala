package perosnzio.controller

import perosnzio.controller.includes.*
import perosnzio.form.PersonForm
import perosnzio.service.PersonService
import zio.*
import zio.http.*
import zio.http.model.{Method, Status}
import zio.json.*

case class PersonController(private val personService: PersonService) {
    private val path = !! / "people"

    def routes = Http.collectZIO[Request] {
        case Method.GET -> path => personService.findAll.toJsonResponse

        case Method.GET -> path / "stream" => personService.findAllStream.toJsonResponse

        case Method.GET -> path / id => personService.findById(id.toInt).toJsonResponse

        case request@Method.POST -> path => request.fromJson[PersonForm](personService.create).toJsonResponse

        case request@Method.PUT -> path / id => request.fromJson[PersonForm](personService.update(id.toInt, _)).toJsonResponse

        case Method.DELETE -> path / id => personService.delete(id.toInt).toResponse(Status.NoContent)
    }
}

object PersonController {
    lazy val layer = ZLayer.fromFunction(apply _)
}
