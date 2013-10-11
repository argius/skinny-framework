package skinny.orm.feature

import scalikejdbc._, SQLInterpolation._

trait SoftDeleteWithBooleanFeature[Entity] extends CRUDFeature[Entity] {

  val isDeletedFieldName = "isDeleted"

  override def defaultScopeWithoutAlias: Option[SQLSyntax] = {
    val scope = sqls.eq(defaultAlias.support.column.field(isDeletedFieldName), false)
    super.defaultScopeWithoutAlias.map(_.and.append(scope)) orElse Some(scope)
  }

  override def defaultScopeWithDefaultAlias: Option[SQLSyntax] = {
    val scope = sqls.eq(defaultAlias.field(isDeletedFieldName), false)
    super.defaultScopeWithDefaultAlias.map(_.and.append(scope)) orElse Some(scope)
  }

  override def deleteBy(where: SQLSyntax)(implicit s: DBSession) {
    updateBy(where).withNamedValues(column.field(isDeletedFieldName) -> true)
  }
}
