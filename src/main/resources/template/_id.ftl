<#macro generate id>
  <#lt>  @Id
  <#lt>  @GeneratedValue(strategy = GenerationType.IDENTITY)
  <#lt>  @Column(name = "${id.getColumnName()}")
  <#lt>  private ${id.getJavaClass()} ${id.getName()};

</#macro>

<#macro generateComp ids >
  <#lt>  @Embeddable
  <#lt>  @Getter
  <#lt>  @Setter
  <#lt>  public static class CompId implements Serializable {
  <#list ids as id>

    <#lt>    @Column(name = "${id.getColumnName()}")
    <#lt>    private ${id.getJavaClass()} ${id.getName()};
  </#list>
  <#lt>  }
  <#lt>  @EmbeddedId
  <#lt>  private CompId compId;

</#macro>
