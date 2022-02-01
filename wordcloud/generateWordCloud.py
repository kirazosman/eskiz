# -*- coding: utf-8 -*-

def generateWordCloud(fileName):
    from wordcloud import WordCloud, STOPWORDS, ImageColorGenerator
    from os import path
    from PIL import Image
    import numpy as np
    import matplotlib.pyplot as plt
    import os
    from wordcloud import WordCloud,  STOPWORDS, ImageColorGenerator
    import time
    import codecs

    d = path.dirname(__file__) if "__file__" in locals() else os.getcwd()
    #stopwords= set(STOPWORDS)
    new_words =codecs.open('stopwords.txt', encoding='utf-8').read().split()
    #new_stopwords=stopwords.union(new_words)
    print(new_words)
    targetImageForMask = "enr_amblem-1080x1080.png" ##Neyin uzerine cizmek istiyorsan
    our_mask = np.array(Image.open(path.join(d, targetImageForMask)))

    text = codecs.open(path.join(d, fileName), encoding='utf-8').read()
    wc = WordCloud(background_color="white",   mask=our_mask,  stopwords=new_words,  max_words=750, collocations=False,prefer_horizontal=0.5, relative_scaling=0)
    wc.generate(text)
    plt.imshow(wc.recolor(color_func=None, random_state=3))
    plt.axis('off')
    plt.savefig(fileName+"_"+targetImageForMask, dpi=500)
	
generateWordCloud("deneme.txt")	
