<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">


<@c.page>

    <#if role == "ADMIN">
        <table class="table-container">
        <thead>
        <tr>
            <th>ID</th>
            <th>Email</th>
            <th>Role</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
            <#list users as usr>
                <tr>
                    <td><a href="/profile?id=${usr.id}">${usr.id}</a></td>
                    <td>${usr.email}</td>
                    <td>${usr.userRole}</td>
                    <td>
                        <form method="post" action="/admin/change-role">
                            <select name="activeRole">
                                <#list roles as r>
                                    <option value="${r}" <#if usr.userRole == r>selected</#if>>${r}</option>
                                </#list>
                            </select>
                            <input type="hidden" name="id" value="${usr.id}">
                            <input type="submit" value="save">
                        </form>
                    </td>

                </tr>
            </#list>
        </tbody>
        </table>

    <#else>

        <h1>Have no access</h1>
    </#if>

</@c.page>