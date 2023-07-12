<#import "parts/common.ftl" as c>
<#import "parts/security.ftl" as sec>
<#include "parts/security.ftl">


<@c.page>

    <@sec.show isAdmin>
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
        <br>
        <br>
        <br>
        <table class="table-container">

            <thead>
            <tr>
                <th>ID</th>
                <th>username</th>
                <th>minecraft name</th>
                <th>user on site</th>
            </tr>
            </thead>
            <tbody>
            <#list discordUsers as usr>
                <tr>
                    <td>${usr.id}</td>
                    <td>${usr.discordUsername}</td>
                    <td><#if usr.mineUsername??>${usr.mineUsername}<#else>-</#if></td>
                    <td>
                        <#if usr.user??>+<#else>-</#if>
                    </td>

                </tr>
            </#list>
            </tbody>
        </table>

    </@sec.show>

</@c.page>