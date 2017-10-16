# -*- coding: utf-8 -*-
"""
Created on Fri Aug 11 17:35:15 2017

@author: javi
"""
import sys
from random import randrange
from datetime import datetime, timedelta

provincias=[[  4, "Almeria",    1],
            [ 11, "Cadiz",      1],
            [ 14, r'Córdoba',   1],
            [ 18, "Granada",    1],
            [ 21, "Huelva",     1],
            [ 23, "Jaen",       1],
            [ 29, "Malaga",     1],
            [ 41, "Sevilla",    1],
            [ 22, "Huesca",     2],
            [ 44, "Teruel",     2],
            [ 50,"Zaragoza",    2],
            [ 33,"Asturias",    3],
            [  7, "Balears, Illes", 4],
            [ 35,"Palmas, Las", 5],
            [ 38,"Santa Cruz de Tenerife", 5],
            [ 39,"Cantabria", 6],
            [  5,"Avila", 7],
            [  9,"Burgos", 7],
            [ 24,r'León', 7],
            [ 34,"Palencia", 7],
            [ 37,"Salamanca", 7],
            [ 40,"Segovia", 7],
            [ 42,"Soria", 7],
            [ 47,"Valladolid", 7],
            [ 49,"Zamora", 7],
            [  2,"Albacete", 8],
            [ 13,"Ciudad Real", 8],
            [ 16,"Cuenca", 8],
            [ 19,"Guadalajara", 8],
            [ 45,"Toledo", 8],
            [  8,"Barcelona", 9],
            [ 17,"Girona", 9],
            [ 25,"Lleida", 9],
            [ 43,"Tarragona", 9],
            [  3,"Alicante/Alacant", 10],
            [ 12,r'Castellón/Castello', 10],
            [ 46,"Valencia", 10],
            [  6,"Badajoz", 11],
            [ 10,"Caceres", 11],
            [ 15,r'Coruña, A', 12],
            [ 27,"Lugo", 12],
            [ 32,"Ourense", 12],
            [ 36,"Pontevedra", 12],
            [ 28,"Madrid", 13],
            [ 30,"Murcia", 14],
            [ 31, "Navarra", 15],
            [  1,"Araba/Alava", 16],
            [ 48,"Bizkaia", 16],
            [ 20,"Gipuzkoa", 16],
            [ 26, "Rioja, La", 17],
            [ 51, "Ceuta", 18],
            [ 52, "Melilla", 19]]

ccaa=[["Andalucia", 1],
      ["Aragon", 2],
      ["Asturias, Principado de", 3],
      ["Balears, Illes", 4],
      ["Canarias", 5],
      ["Cantabria", 6],
      ["Castilla y Leon", 7],
      ["Castilla - La Mancha", 8],
      [r'Cataluña', 9],
      ["Comunitat Valenciana", 10],
      ["Extremadura", 11],
      ["Galicia", 12],
      ["Madrid, Comunidad de", 13],
      ["Murcia, Region de", 14],
      ["Navarra, Comunidad Foral de",  15],
      ["Pais Vasco", 16],
      ["Rioja, La", 17],
      ["Ceuta", 18],
      ["Melilla", 19]]

farmaco=['NPK 1','NPK 2', 'NPK 3', 'NPK 4', 'NPK 5', 'NPK 6',
         'HKR 1', 'HKR 2', 'HKR 3', 'JKP 1', 'JKP 2', 'JKP 3',
         'OIU 1', 'OIU 2', 'OIU 3', 'QRE 1', 'QRE 2', 'QRE 3', 
         'ZOI 1', 'ZOI 1', 'AJF 1', 'AJF 2', 'AJF 3', 'AJF 4', 
         'AJF 5', 'BDD 1', 'BDD 2', 'BDD 3', 'BDD 4', 'BDD 5',
         'CFR 1', 'CFR 2', 'CFR 3', 'PHU 1', 'PHU 2', 'PHU 3',
         'MJH 1', 'MJH 2', 'MJH 3', 'MJH 4', 'MJH 5', 'MJH 6',
         'HAL 1', 'HAL 2', 'HAL 3', 'HAL 4', 'HAL 5', 'HAL 6',
         'GAM 1', 'GAM 2', 'GAM 3', 'GAM 4', 'GAM 5', 'GAM 6',
         'VAL 1', 'VAL 2', 'VAL 3', 'VAL 4', 'VAL 5', 'VAL 6',
         'TRK 1', 'TRK 2', 'TRK 3', 'TRK 4', 'TRK 5', 'TRK 6',
         'TRE 1', 'TRE 2', 'TRE 3', 'TRE 4', 'TRE 5', 'TRE 6' ]

dosis=[['1mg', 1],['2mg', 2], ['3mg', 3], ['4mg', 4], ['5mg', 5]]

costefarmaco=[ 12, 30, 40 ]

hospital=['Centro 1','Centro 2', 'Centro 3', 'Centro 4', 'Centro 5', 'Centro 6', 'Centro 7', 'Centro 8', 'Centro 9', 
          'Centro 10', 'Centro 11', 'Centro 12', 'Centro 13', 'Centro 14', 'Centro 15', 'Centro 16', 'Centro 17', 
          'Centro 18', 'Centro 19', 'Centro 20']

enfermedad=[r'Tensión arterial', 'Infarto miocardio', 'Angina de Pecho', 'Problemas cardiacos',
            'Problemas circulatorios', 'Artrosis', 'Espalda Cervical', 'Lumbares', 'Alergia',
            r'Asma alérgica', 'EPOC', 'Diabetes', 'Problemas estomago', r'Urología', 'Colesterol',
            'Cataratas', r'Problemas dermatológicos', r'Problemas hepáticos', r'Depresión',
            'Ansiedad', 'Problemas Mentales', 'Vascular Cerebro', r'Migraña', 'Tumores',
            'Osteoporosis', 'Tiroides', r'Problemas de Riñón']

fechasLoad=[[2010, 1, 2, 1, 1 ], [2010, 4, 90, 2, 90 ], [2010, 7, 181, 3, 181 ], [2010, 10, 273, 4, 273 ],
            [2011, 1, 365, 1, 365 ], [2011, 4, 455, 2, 455 ], [2011, 7, 546, 3, 546 ], [2011, 10, 638, 4, 638 ],
            [2012, 1, 730, 1, 730 ], [2012, 4, 821, 2, 821 ], [2012, 7, 913, 3, 913 ], [2012, 10, 1005, 4, 1005 ],
            [2013, 1, 1096, 1, 1096 ], [2013, 4, 1186, 2, 1186 ], [2013, 7, 1286, 3, 1286 ], [2013, 10, 1370, 4, 1370 ],
            [2014, 1, 1461, 1, 1461 ], [2014, 4, 1551, 2, 1551 ], [2014, 7, 1642, 3, 1642 ], [2014, 10, 1734, 4, 1734 ],
            [2015, 1, 1826, 1, 1826 ], [2015, 4, 1916, 2, 1916 ], [2015, 7, 2007, 3, 2007 ], [2015, 10, 2099, 4, 2099 ],
            [2016, 1, 2191, 1, 2191 ], [2016, 4, 2282, 2, 2282 ], [2016, 7, 2373, 3, 2373 ], [2016, 10, 2465, 4, 2465 ],
            [2017, 1, 2557, 1, 2557 ], [2017, 4, 2647, 2, 2647 ], [2017, 7, 2738, 3, 2738 ], [2017, 10, 2830, 4, 2830 ],
            [2018, 1, 2922, 1, 2922 ]]

estado=['Curado', 'Enfermo','Crónico']

def GetIdFecha(anio, mes ):
    idFecha = 1
    for i in fechasLoad:
        # Elementos de cada entrada
        if i[0]==anio and i[1] < mes:
            idFecha = randrange(i[4],i[4]+85)
    return idFecha

def GenerarCodigoPaciente(nword):
        tword = []
        while int(len(tword)) < nword:
            tword.append(chr(randrange(97,122)))
            if nword < int(len(tword)):
                break
        return ''.join(str(e) for e in tword)

def Fecha(start, end, fmt = "%d/%m/%Y"):
    s = datetime.strptime(start, fmt)
    e = datetime.strptime(end, fmt)    
    delta = e - s
    while True:
        r = s + timedelta(days=(randrange(0,12) * 30))
        midelta = e - r
        if midelta.days < delta.days:
            break
    return r.strftime(fmt)

def Hospital():
    return hospital[randrange(0,len(hospital))]

def GenerarEntero(maximo):
    return randrange(1,maximo)

def Edad():
    return str(randrange(0,95))

def Sexo():
    VNom=['F','M']
    return VNom[randrange(0,len(VNom))]

def Dolencia():
    return enfermedad[randrange(0,len(enfermedad))]

def Farmaco():
    return farmaco[randrange(0,len(farmaco))]

def Dosis():
    return dosis[randrange(0,len(dosis))]

def CosteFarmaco():
    return costefarmaco[randrange(0,len(costefarmaco))]

def Duracion():
    return randrange(1,12)

def GetIdEstado():
    return randrange(1,4)

def GenerarBooleano():
    VNom=[True , False]
    return VNom[randrange(0,len(VNom))]

def Diagnosticado():
    VNom=[True , False]
    return VNom[randrange(0,len(VNom))]

def creaCCAA():
    try:
        f=open('ccca.csv','w+')
        f.write('Id;Comunidad'+'\n')        
        for i in ccaa:
            # Elementos de cada entrada
            f.write(str(i[1])+';'+i[0]+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaCCAA')
        sys.exit(-1)

def creaProvincias():
    try:
        f=open('provincia.csv','w+')
        f.write('Id;Provincia;IdCCAA'+'\n')        
        for i in provincias:
            # Elementos de cada entrada
            f.write(str(i[0])+';'+i[1]+';'+str(i[2])+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaProvincias')
        sys.exit(-1)

def creaDosis():
    try:
        f=open('dosis.csv','w+')
        f.write('NombreDosis;Dosis'+'\n')        
        for y in dosis:
            # Elementos de cada entrada
            f.write(y[0]+';'+str(y[1])+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaDosis')
        sys.exit(-1)

def creaFarmacos():
    try:
        f=open('farmaco.csv','w+')
        f.write('IdDosis;Farmaco;Coste Farmaco'+'\n')        
        for x in farmaco:
            for i in range(5):
                for z in costefarmaco:
                    # Elementos de cada entrada
                    f.write(str(i+1)+';'+x+';'+str(z)+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaFarmacos')
        sys.exit(-1)

def creaHospital():
    try:
        f=open('hospital.csv','w+')
        f.write('Hospital;Pacientes;ControlHospitalario'+'\n')        
        for x in hospital:
            # Elementos de cada entrada
            f.write(x+';'+str(GenerarEntero(4000))+';'+str(GenerarBooleano())+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaHospital')
        sys.exit(-1)
        
def creaEnfermedad():
    try:
        f=open('enfermedad.csv','w+')
        f.write('Dolencia;Hospitalizacion'+'\n')        
        for x in enfermedad:
            # Elementos de cada entrada
            f.write(x+';'+str(GenerarBooleano())+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaEnfermedad')
        sys.exit(-1)

def creaEstado():
    try:
        f=open('estado.csv','w+')
        f.write('Estado'+'\n')        
        for x in estado:
            # Elementos de cada entrada
            f.write(x+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaEstado')
        sys.exit(-1)

def creaF(fichero, num, anio, mes, inicio, final, diagnostico, primera = False):
    try:
        f=open(fichero,'a')
        if primera:
            f.write('IdFecha;FechaInicio;FechaFin;FechaDiagnostico;IdCCAA;IdProv;Hospital;Dolencia;Farmaco;Sexo;Edad;CodigoPaciente;Coste Farmaco;Duracion;Estado;Dosis;Placebo;Diagnosticado'+'\n')
        
        for i in range(num):
            # Elementos de cada entrada
            fechaInicio = Fecha(inicio, final)
            fechaFin = Fecha(fechaInicio, final)
            diagnosticado = Diagnosticado()
            fechaDiagnostico= ''
            if diagnosticado:
                fechaDiagnostico= Fecha(diagnostico, fechaInicio)                
            ccaa_prov =provincias[randrange(0,len(provincias))]
            duracion = Duracion()
            costefarmaco = CosteFarmaco()
            idFecha = GetIdFecha(anio, mes)
            idDosis = str(Dosis()[1])
            f.write(str(idFecha)+';'+str(fechaInicio)+';'+str(fechaFin)+';'+str(fechaDiagnostico)+';'+str(ccaa_prov[2])+';'+str(ccaa_prov[0])+';'+
                    Hospital()+';'+Dolencia()+';'+Farmaco()+';'+Sexo()+';'+Edad()+';'+GenerarCodigoPaciente(10)+';'+
                    str(costefarmaco)+';'+str(duracion)+';'+str(GetIdEstado())+';'+idDosis+';'+str(GenerarBooleano())+';'+
                    str(diagnosticado)+'\n')
        f.close()
    except Exception as e:
        print('Exception en funcion de creaF')
        sys.exit(-1)

tamanio = 20000

#creaEnfermedad()
#creaFarmacos()
#creaHospital()
#creaCCAA()
#creaProvincias()
#creaEstado()
#creaDosis()
 
creaF('output1.csv', tamanio, 2011, 2, '01/01/2009', '31/12/2009', '01/01/2008', True)
creaF('output1.csv', tamanio, 2011, 4, '01/01/2009', '31/12/2009', '01/01/2008', False)
creaF('output1.csv', tamanio, 2011, 8, '01/01/2009', '31/12/2009', '01/01/2008', False)
creaF('output1.csv', tamanio, 2011,11, '01/01/2009', '31/12/2009', '01/01/2008', False)
 
#==============================================================================
# creaF('output1.csv', tamanio, 2012, 2, '01/01/2010', '31/12/2010', '01/01/2009', True)
# creaF('output1.csv', tamanio, 2012, 4, '01/01/2010', '31/12/2010', '01/01/2009', False)
# creaF('output1.csv', tamanio, 2012, 8, '01/01/2010', '31/12/2010', '01/01/2009', False)
# creaF('output1.csv', tamanio, 2012,11, '01/01/2010', '31/12/2010', '01/01/2009', False)
# 
# creaF('output1.csv', tamanio, 2013, 2, '01/01/2011', '31/12/2011', '01/01/2010', True)
# creaF('output1.csv', tamanio, 2013, 4, '01/01/2011', '31/12/2011', '01/01/2010', False)
# creaF('output1.csv', tamanio, 2013, 8, '01/01/2011', '31/12/2011', '01/01/2010', False)
# creaF('output1.csv', tamanio, 2013,11, '01/01/2011', '31/12/2011', '01/01/2010', False)
#     
# creaF('output1.csv', tamanio, 2014, 2, '01/01/2012', '31/12/2012', '01/01/2011', True)
# creaF('output1.csv', tamanio, 2014, 4, '01/01/2012', '31/12/2012', '01/01/2011', False)
# creaF('output1.csv', tamanio, 2014, 8, '01/01/2012', '31/12/2012', '01/01/2011', False)
# creaF('output1.csv', tamanio, 2014,11, '01/01/2012', '31/12/2012', '01/01/2011', False)
#    
# creaF('output1.csv', tamanio, 2015, 2, '01/01/2013', '31/12/2013', '01/01/2012', True)
# creaF('output1.csv', tamanio, 2015, 4, '01/01/2013', '31/12/2013', '01/01/2012', False)
# creaF('output1.csv', tamanio, 2015, 8, '01/01/2013', '31/12/2013', '01/01/2012', False)
# creaF('output1.csv', tamanio, 2015,11, '01/01/2013', '31/12/2013', '01/01/2012', False)
#    
# creaF('output1.csv', tamanio, 2016, 2, '01/01/2014', '31/12/2014', '01/01/2013', True)
# creaF('output1.csv', tamanio, 2016, 4, '01/01/2014', '31/12/2014', '01/01/2013', False)
# creaF('output1.csv', tamanio, 2016, 8, '01/01/2014', '31/12/2014', '01/01/2013', False)
# creaF('output1.csv', tamanio, 2016,11, '01/01/2014', '31/12/2014', '01/01/2013', False)
#    
# creaF('output1.csv', tamanio, 2017, 2, '01/01/2016', '31/12/2016', '01/01/2015', True)
# creaF('output1.csv', tamanio, 2017, 4, '01/01/2016', '31/12/2016', '01/01/2015', False)
# creaF('output1.csv', tamanio, 2017, 8, '01/01/2016', '31/12/2016', '01/01/2015', False)
#==============================================================================

