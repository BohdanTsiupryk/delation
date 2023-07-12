<#assign isAuth = principal??>
<#assign sameUser = false>
<#assign isAdmin = false>
<#assign isModer = false>

<#if isAuth && principal != "anonymousUser">
        <#assign logged = true>
        <#assign role = principal.role>
        <#if principal.role == "ADMIN"> <#assign isAdmin = true></#if>
        <#if principal.role == "MODER"> <#assign isModer = true></#if>
<#else>
    <#assign logged = false>
    <#assign role = "UNKNOWN">
</#if>

<#macro show expression>
    <#if expression>
        <#nested>
    <#else >
    </#if>
</#macro>