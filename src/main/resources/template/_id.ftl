<#import "_field.ftl" as _field>

<#macro generate id >
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  <@_field.generate id "  "/>
</#macro>

<#macro generateComp ids >
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
</#macro>
