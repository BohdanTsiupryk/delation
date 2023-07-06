<#include "parts/security.ftl">
<#import "parts/common.ftl" as c>


<@c.page>
    <#if role == "ADMIN" || role == "MODER">

        <#if filterCurrentStatus??> <#assign currentStatus = filterCurrentStatus><#else><#assign currentStatus = ''> </#if>
        <#if filterCurrentType??> <#assign currentType = filterCurrentType><#else><#assign currentType = ''> </#if>

        <div>
            <a href="/moder/feedback">clear filters</a>
        </div>
        <div class="mb-4">
            <h5>Filters</h5>
            <form action="/moder/feedback" method="get">
                <div class="form-row">
                    <div class="col">
                        <label for="categoryFilter">Category:</label>
                            <select name="type" class="form-control" id="categoryFilter">
                                <option value="">---</option>
                                <#list types as t>
                                    <option <#if t == currentType>selected</#if>value="${t}">${t.getUa()}</option>
                                </#list>
                            </select>
                    </div>
                    <div class="col">
                        <label for="statusFilter">Status:</label>
                            <select name="status" class="form-control" id="statusFilter">
                                <option value="">---</option>
                                <#list statuses as s>
                                    <option <#if s == currentStatus>selected</#if> value="${s}">${s}</option>
                                </#list>
                            </select>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary mt-2">Apply Filters</button>
            </form>
        </div>


        <nav aria-label="Page navigation">
            <ul class="pagination">
                <#list listPageNumbers as numb>
                    <li class="page-item <#if numb == currentPage>active</#if>">
                        <a class="page-link" href="/moder/feedback?page=${numb}&type=${currentType}&status=${currentStatus}">${numb+1}</a>
                    </li>
                </#list>
            </ul>
        </nav>


        <table class="table table-striped">
            <thead>

            <tr>
                <th>ID</th>
                <th>type</th>
                <th>text</th>
                <th>author</th>
                <th>assigned</th>
                <th>mentions</th>
                <th>status</th>
                <th>created</th>
            </tr>
            </thead>
            <tbody>
            <#list list as l>
                <tr>
                    <td title="${l.id()}"><a href="/moder/feedback/${l.id()}" target="_blank">${l.id()}</a></td>
                    <td>${l.type().getUa()}</td>
                    <td class="text-limit" title="${l.text()}">${l.text()}</td>
                    <td>${l.author()}</td>
                    <td>
                        <form method="post" action="/moder/feedback/assign?from=list">
                            <select name="moder">
                                <option value="none" selected>none</option>
                                <#list moders as mod>
                                    <option value="${mod.id()}"
                                            <#if mod.name() == l.moder()>selected</#if>>${mod.name()}</option>
                                </#list>
                            </select>
                            <input type="hidden" name="id" value="${l.id()}">
                            <input type="submit" value="assign">
                        </form>
                    </td>
                    <td><#list l.mentions() as m>@${m}<#sep>,</#list></td>
                    <td>
                        ${l.status()}
                    </td>
                    <td>${l.date()?string("HH:mm:ss dd/MM/yyyy")}</td>
                </tr>
            </#list>
            </tbody>
        </table>

    </#if>


</@c.page>