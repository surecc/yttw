# import qrtools

# qr = qrtools.QR()
# qr.decode("./000017.jpg")
# print qr.data



#!/usr/bin/env python
# coding: utf-8
#
# pyqrcode sample decoder

import sys, qrcode

d = qrcode.Decoder()
if d.decode('./000017.jpg'):
    print 'result: ' + d.result
else:
    print 'error: ' + d.error