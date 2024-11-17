<#import "_id.ftl" as _id>
<#import "_field.ftl" as _field>
<#import "_relationship.ftl" as _relationship>

package ${packageName};

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
<#list entity.getImports() as import>
  <#lt>import ${import};
</#list>

@Getter
@Setter
@Entity
@Table(name = "${entity.getTableName()}")
public class ${entity.getName()} {
  public static final String TABLE_NAME = "${entity.getTableName()}"

  <#if (entity.getIds()?size > 1)>
    <@_id.generateComp entity.getIds() />
  <#elseif (entity.getIds()?size = 1)>
    <@_id.generate entity.getIds()[0] />
  </#if>

  <#if (entity.getFields()?size != 0)>
    <#list entity.getFields() as entityField>
      <@_field.generate entityField "  "/>
    </#list>
  </#if>

  <#if (entity.getCompRelationships()?size != 0)>
    <#list entity.getRelationships() as relationship>
      <@_relationship.generate relationship "  "/>
    </#list>
  </#if>

  <#if (entity.getCompRelationships()?size != 0)>
    <#list entity.getCompRelationships() as relationship>
      <@_relationship.generateComp relationship "  "/>
    </#list>
  </#if>
}
