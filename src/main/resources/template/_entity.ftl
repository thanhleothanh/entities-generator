<#import "_id.ftl" as _id>
<#import "_field.ftl" as _field>
package ${packageName};

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

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
}
