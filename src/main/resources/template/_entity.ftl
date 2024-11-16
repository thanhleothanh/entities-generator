package ${packageName};

import java.math.BigDecimal;
import java.time.Instant;
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

<#list entity.getFields() as entityField>
  <#import "_field.ftl" as _field>
  <#assign field=entityField in _field>
  <@_field.generate />
</#list>
}
