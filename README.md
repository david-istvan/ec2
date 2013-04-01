EC2
======================================

EC stands for "Early classifiability" of time series. This framework has been used for analysing labeled time series and investigate how the predicted label for the whole series varies when using segments of the series of different length.

The related abstract from the GfKl2012 conference
-------------------------------------
Although classification of time series or sequences of observations is one of the well-studied topics, there are just a few works on their early classification. By early classifiability we mean the property that the class label (which refers to the entire time series) can be often predicted based on the first few observations (see e.g. Xing et al., 2009 and Xing et al., 2008). Some practical examples of early classification include fraud detection, applications in health care and network engineering (e.g. classification of TCP/IP packets based on the first few segments of data).
In our study, we examine the relation of early classifiability to early identification of clusters and cluster (in)stability, which might be an indicator of concept drift.
As the k-nearest neighbor algorithm (k-NN) with dynamic time warping (DTW) became popular for time-series classification, we target the above question in the context of k-NN and k-medoids. Furthermore, we extend the concept of cluster stability introduced by Ackerman and Ben-David (2009) for time-series clustering.

Implementation
--------------
Bare Java, nothing fancy.

License
-------
[Eclipse Public License v1.0] (http://www.eclipse.org/legal/epl-v10.html)
