<#import "_id.ftl" as _id>
<#import "_field.ftl" as _field>
<#import "_referencingField.ftl" as _referencingField>
package ${packageName};

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
<#list entity.getImports() as import>
import ${import};
</#list>

@Getter
@Setter
@Entity
@Table(name = "${entity.getTableName()}")
public class ${entity.getName()} {
  public static final String TABLE_NAME = "${entity.getTableName()}"

  <@_id.generate entity.getIds() entity.getIds()?size />
  <#list entity.getFields() as entityField>
    <@_field.generate entityField "  "/>
  </#list>
  <#list entity.getReferencingFields() as referencingField>
    <@_referencingField.generate referencingField "  "/>
  </#list>
}
