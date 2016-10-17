package com.pronvis.mnist_by_humans.db
import com.pronvis.mnist_by_humans.db.Entities.ImageData

import scala.concurrent.Future
//todo: own executionContext for DBStuff
import scala.concurrent.ExecutionContext.Implicits.global

class ImagesDataDao(schema: Schema) {

  import schema.context._

  def insertImage(imageData: ImageData): Future[Long] = schema.context.run{quote {
    schema.imagesData.insert(lift(imageData)).returning(_.id)
  }}

  def insertImages(imagesData: List[ImageData]): Future[List[Long]] = schema.context.run{quote {
    liftQuery(imagesData).foreach(imgData =>
      schema.imagesData.insert(imgData).returning(_.id)
    )
  }}

}
