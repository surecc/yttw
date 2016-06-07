#!/usr/bin/env python
# coding: utf-8
# 
from PIL import Image
import zbarlight
import os
import sys

# read the qr code info from the jpg files 
def read_qrcode(pic_name):
    with open(pic_name, 'rb') as image_file:
        image = Image.open(image_file)
        image.load()
    converted_image = image.convert('L')  # Convert image to gray scale (8 bits per pixel).
    image.close()

    raw = converted_image.tobytes()  # Get image data.
    width, height = converted_image.size  # Get image size.
    code = zbarlight.qr_code_scanner(raw, width, height)
    print code
    if code != None:
        pic_id = get_pic_id(code)
        return pic_id
        # way_2 : chose the number and ab
        
    else:
        print '[No Need to Rename] pic_name: ' + pic_name
        return ''

# read the qr code info from the jpg files 
def read_qrcode_new(pic_name):
    with open(pic_name, 'rb') as image_file:
        image = Image.open(image_file)
        image.load()
    codes = zbarlight.scan_codes('qrcode', image)
    print 'codes:' + codes
    if codes != None:
        pic_id = get_pic_id(codes)
        return pic_id
    else:
        print '[No Need to Rename] pic_name: ' + pic_name
        return ''

def get_pic_id(str_ori):
    array_code = str_ori.split('|')
    pic_id = array_code[-4]
    print 'pic_id: ' + pic_id
    return pic_id


# convert the pdf files into jpg
def conv_pdf_jpg(pdf_file_name):
    print 'conv_pdf_jpg: ' + pdf_file_name
    pdf = file(pdf_file_name, "rb").read()

    startmark = "\xff\xd8"
    startfix = 0
    endmark = "\xff\xd9"
    endfix = 2
    i = 0

    njpg = 0
    while True:
        istream = pdf.find("stream", i)
        if istream < 0:
            break
        istart = pdf.find(startmark, istream, istream+20)
        if istart < 0:
            i = istream+20
            continue
        iend = pdf.find("endstream", istart)
        if iend < 0:
            raise Exception("Didn't find end of stream!")
        iend = pdf.find(endmark, iend-20)
        if iend < 0:
            raise Exception("Didn't find end of JPG!")
         
        istart += startfix
        iend += endfix
        print "JPG %d from %d to %d" % (njpg, istart, iend)
        jpg = pdf[istart:iend]
        jpg_file_name = pdf_file_name.split('.pdf')[0]+".jpg"
        jpgfile = file(jpg_file_name, "wb")
        jpgfile.write(jpg)
        jpgfile.close()
        
        njpg += 1
        i = iend
    return jpg_file_name

def main(path_root):
    # 1. read all the pdf files
    # rootDir = sys.argv[1]
    array_pdf_file = []
    list_dirs = os.walk(path_root)
    for root, dirs, files in list_dirs:
        for f in files:
            file_name = os.path.join(root, f)
            if file_name.find('.pdf') != -1:
                array_pdf_file.append(file_name)

    # 2. convert the pdf file into jpg
    for pdf_file in array_pdf_file:
        
        jpg_file = conv_pdf_jpg(pdf_file)
        print 'jpg_file: ' + jpg_file + '\n'
        dir_name = os.path.dirname(jpg_file)
        # 3. get and read the jpg qr code into string
        jpg_id = read_qrcode(jpg_file)
        if jpg_id != '':
            new_pdf_name = jpg_id+".pdf"
            new_pdf_file = os.path.join(dir_name,new_pdf_name)
            # 4. rename it
            print 'pdf_file' + pdf_file
            print 'new_pdf_file' + new_pdf_file
            os.renames(pdf_file,new_pdf_file)
        # 5. delete the tmp jpg
        if os.path.isfile(jpg_file):
            os.remove(jpg_file)

if __name__ == '__main__':
    path_root = raw_input("请输入根目录路径： ");
    path_root = path_root.strip()
    print os.path.isdir(path_root) 
    if os.path.isdir(path_root):
        main(path_root)
    else:
        print "请输入正确的路径！"
        exit

    # conv_pdf_jpg('./000017.pdf')
    # read_qrcode_new('./000017.jpg')


