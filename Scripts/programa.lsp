;; Generador de horarios universitarios
(SETQ aulas (QUOTE (
    ("A101" 50 "Proyector")
    ("B202" 30 "Computadoras")
)))

(DEFUN asignar-curso (curso)
    (COND 
        ((> (random 2) 0)  ; Simula disponibilidad aleatoria
            (SETQ horarios (CONS curso horarios))
            (print "Curso asignado: " curso)
        (T 
            (print "Curso en espera: " curso))))

;; Cursos a asignar
(asignar-curso '("Matemáticas" "Lunes 8:00" "A101"))
(asignar-curso '("Física" "Martes 10:00" "B202"))

horarios  ; Retorna la lista final