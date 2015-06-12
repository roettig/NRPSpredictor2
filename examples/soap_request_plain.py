import httplib

body="""<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                       xmlns:ws="http://ws.NRPSpredictor2.roettig.org/">
   <soapenv:Header/>
   <soapenv:Body>
      <ws:predict>
         <ws:request>
            <ws:sequence>
               <ws:id>1</ws:id>
               <ws:kingdom>bacterial</ws:kingdom>
               <ws:seqString>HWMTFDASVWELQMFCGGEINLYGPTETTIDATY</ws:seqString>
               <ws:sequenceType>Signature8A</ws:sequenceType>
            </ws:sequence>
            <ws:sequence>
               <ws:id>2</ws:id>
               <ws:kingdom>bacterial</ws:kingdom>
               <ws:seqString>HFMTFDGSVWELQMFCGGEINLYGPTETTIDATY</ws:seqString>
               <ws:sequenceType>Signature8A</ws:sequenceType>
            </ws:sequence>
         </ws:request>

      </ws:predict>
   </soapenv:Body>
</soapenv:Envelope>"""

headers = {"User-Agent":"Python post","SOAPAction":"\"\""}
conn    = httplib.HTTPConnection("localhost",9999)

conn.request("POST", "/ws/nrpspredictor2service", body, headers)

response = conn.getresponse()

print response.status, response.reason
print response.msg
data = response.read()
print data