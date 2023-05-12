<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">


<@c.page>

    <#if role == "ADMIN">
        <ol>

            <#list users as usr>
                <li>${usr.id} - ${usr.email} - ${usr.userRole} -

                    <form method="post" action="/admin/change-role">
                        <select name="activeRole">
                            <#list roles as r>
                                <option value="${r}" <#if usr.userRole == r>selected</#if>>${r}</option>
                            </#list>
                        </select>
                        <input type="hidden" name="id" value="${usr.id}">
                        <input type="submit" value="save">
                    </form>
                </li>

            </#list>

        </ol>

    <#else>

        <h1>Have no access</h1>
    </#if>

</@c.page>