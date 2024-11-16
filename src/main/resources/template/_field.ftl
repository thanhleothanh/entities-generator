<#macro generate>
<#if field.getIsId()>
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
</#if>
  @Column(name = "${field.getColumnName()}")
  private ${field.getDataType()} ${field.getName()};

</#macro>