<#import "_field.ftl" as _field>

<#macro generate ids totalNumberOfIds>
  <#if (totalNumberOfIds > 1)>
  @Embeddable
  @Getter
  @Setter
  public static class CompId {
    <#list ids as id>
      <@_field.generate id "    "/>
    </#list>
  }

  @EmbeddedId
  private CompId compId;

  <#else>
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  <#list ids as id>
    <@_field.generate id "  "/>
  </#list>
  </#if>
</#macro>