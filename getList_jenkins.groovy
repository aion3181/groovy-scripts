@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1')

import static groovyx.net.http.ContentType.TEXT
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.Method.POST


def user = 'admin'
def password = 'admin123'
def myrepo = "MNT-maven2-hosted-releases"
def artifacts = []
String basicAuthString = "Basic " + "$user:$password".bytes.encodeBase64().toString()

def reqmap =  """ { "action": "coreui_Component", "method":"readAssets",
                "data":[{"page":"1", "start":"0",    "limit":"300",
                "sort": [{"property":"name","direction":"ASC"}],
                "filter": [{"property":"repositoryName",
                "value":"$myrepo"}]}],
                "type":"rpc", "tid":15 } """


def reqartf = new HTTPBuilder("http://nexus:8081")

reqartf.request(POST, TEXT) { req ->
    headers."Authorization" = basicAuthString
    uri.path = "/service/extdirect"
    headers."Content-Type"="application/json"
    body = reqmap

    response.success = { resp, json ->
        def slurper = new groovy.json.JsonSlurper()
        def jsonT2S = json.text as String
        def jsonParse = slurper.parseText(jsonT2S)
        jsonParse.result.data.each {
            if (it.name.matches(~/.+.tar.gz/)) {
                artifacts.add(it.name)
            }
        }
    }
}
artifacts
