<#import "_id.ftl" as _id>
<#import "_relationship.ftl" as _relationship>

package ${packageName};

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
<#list entity.getImports() as import>
  <#lt>import ${import};
</#list>

@Getter
@Setter
@Entity
@Table(name = "${entity.getTableName()}")
public class ${entity.getName()} implements Serializable {
  public static final String TABLE_NAME = "${entity.getTableName()}";

  <#--id-->
  <#if (entity.getIds()?size > 1)><@_id.generateComp entity.getIds() />
  <#elseif (entity.getIds()?size = 1)><@_id.generate entity.getIds()[0] />
  </#if>
  <#--normal fields-->
  <#if (entity.getFields()?size != 0)>
    <#list entity.getFields() as field>
      <#lt>  @Column(name = "${field.getColumnName()}")
      <#lt>  private ${field.getJavaClass()} ${field.getName()};

    </#list>
  </#if>
  <#--relationships-->
  <#if (entity.getCompRelationships()?size != 0)>
    <#list entity.getRelationships() as relationship><@_relationship.generate relationship/></#list>
  </#if>
  <#--composite relationships -->
  <#if (entity.getCompRelationships()?size != 0)>
    <#list entity.getCompRelationships() as relationship><@_relationship.generateComp relationship/></#list>
  </#if>
}
