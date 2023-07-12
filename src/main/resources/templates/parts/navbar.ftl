<#include "security.ftl">
<#import "security.ftl" as sec>

<nav>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="/index">Delation</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <@sec.show logged>
                    <li class="nav-item">
                        <a class="nav-link" href="/profile">Profile</a>
                    </li>
                </@sec.show>
                <@sec.show (isModer || isAdmin)>
                    <li class="nav-item">
                        <a class="nav-link" href="/moder/feedback">Appeals</a>
                    </li>
                </@sec.show>

                <li class="nav-item">
                    <a class="nav-link" href="/public/feedbacks">Feedback List</a>
                </li>
                <@sec.show isAdmin>
                    <li class="nav-item">
                        <a class="nav-link" href="/admin">Admin Page</a>
                    </li>
                </@sec.show>
            </ul>

            <ul class="navbar-nav ml-auto">
                <@sec.show logged>
                    <li class="nav-item">
                        <a class="nav-link" href="/logout">Logout</a>
                    </li>
                </@sec.show>
            </ul>
        </div>
    </nav>
</nav>