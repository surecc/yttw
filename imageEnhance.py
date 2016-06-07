#!/usr/bin/env python
# coding: utf-8
# 
from pytesser import *
import ImageEnhance

image = Image.open('./000017.jpg')

#使用ImageEnhance可以增强图片的识别率
enhancer = ImageEnhance.Contrast(image)
image_enhancer = enhancer.enhance(4)

print image_to_string(image_enhancer)