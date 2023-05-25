<#include "security.ftl">
<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8"/>
        <title>Index</title>
        <style>
            table {
                font-family: arial, sans-serif;
                border-collapse: collapse;
                width: 100%;
            }

            td, th {
                border: 1px solid #dddddd;
                text-align: left;
                padding: 8px;
            }

            tr:nth-child(even) {
                background-color: #dddddd;
            }

            .text-min-limit {
                min-width: 100px; /* Мінімальна ширина текстового елемента */
            }
            .text-limit {
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                max-width: 200px; /* Приклад: обмежити текст до 200px ширини */
            }
        </style>
    </head>
    <body>
    <#--    <#include "navbar.ftl">-->
    <div class="container mt-5">
        <#nested>
    </div>
    </body>
    </html>
</#macro>