from pysimplesoap.client import SoapClient
from pysimplesoap.simplexml import SimpleXMLElement

def makeNRPSrequest(seqs):
    root = SimpleXMLElement("<predict></predict>",prefix="ws",namespace="http://ws.NRPSpredictor2.roettig.org/")
    root.add_child("request",ns=True)
    print root.as_xml(pretty=True)
    arg0_node = root.children()[0]
    

    idx = 1
    for seq in seqs:
        seqs_node = arg0_node.add_child("sequence",ns=True)
        seqs_node.add_child("id","%d"%idx,ns=True)
        seqs_node.add_child("kingdom","0",ns=True)
        seqs_node.add_child("seqString",seq,ns=True)
        seqs_node.add_child("sequenceType","0",ns=True)
        idx+=1
    print root.as_xml(pretty=True)    
    return root
        

client = SoapClient(ns="ws",wsdl="http://localhost:9999/ws/nrpspredictor2service?wsdl",trace=True)

client.location = "http://localhost:9999/ws/nrpspredictor2service"

## predict svc
req = makeNRPSrequest(["HWMTFDASVWELQMFCGGEINLYGPTETTIDATY","AWMEFDASVWELQMFCGGEINLYGPTETTIDATY"])
client.call("predict",req)