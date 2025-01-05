def egcd(a, b):
    x,y, u,v = 0,1, 1,0
    while a != 0:
        q, r = b//a, b%a
        m, n = x-u*q, y-v*q
        b,a, x,y, u,v = a,r, u,v, m,n
    gcd = b
    return gcd, x, y

A=94
B=22
X=8400

gcd, a, b = egcd(A, B)

print("The gcd of", A, "and", B, "is", gcd)
print("Coefficients are", a, b)

scale_factor = X // gcd
a0 = a * scale_factor
b0 = b * scale_factor

for k in range(223403, 223404):
    ak = a0 + k*B
    bk = b0 - k*A
    print("k =", k, ":", ak, bk, ak*A + bk*B)
