<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="iso-8859-1" %>
<jsp:include page="/header.jsp"/>
<jsp:include page="/cadastros/menuLogado.jsp"/>

<div class="container-fluid">
    <h1 class="h3 mb-2 text-gray-800">Despesas</h1>
    <p class="mb-4">Planilha de Registros</p>

    <a class="btn btn-success mb-4" href="${pageContext.request.contextPath}/DespesaNovo">
        <i class="fas fa-sticky-note"></i>
        <strong>Novo</strong>
    </a>
    <div class="row">
        <div class="col">
            <div class="card shadow md-4">
                <div class="card-body">
                    <div class="form-group">
                        <center>
                            <img alt="imagem" class="imag-thumbnail" src="${despesa.imagemDocumento}" name="target" id="target" width="170" heigth="200"/>
                            <br/><br/>
                            <input type="file" id="gallery-photo-add" class="inputfile" onchange="uploadFile();"/>
                            <label for="gallery-photo-add" class="btn btn-success">
                                <i class="fas fa-file-upload"></i>
                                Selecionar Capa...
                            </label>
                        </center>
                    </div>
                </div>                
            </div>            
        </div>
        <div class="col-lg-9">
            <div class="card shadow mb-4">
                <div class="card-body">
                    <div class="form-group">
                        <label>ID</label>
                        <input class="form-control" type="hidden" name="iddespesa" id="iddespesa" value="${despesa.idDespesa}" readonly="readonly"/>
                    </div>
                    <div class="form-group">
                        <label>Descrição da Despesa</label>
                        <input class="form-control" type="text" name="descricao" id="descricao" value="${despesa.descricao}" size="100" maslength="100"/>
                    </div>
                    <div class="form-group">
                        <div class="form-line row">
                            <div class="col-sm">
                                <label>Data da Despesa</label>
                                <input class="form-control" type="date" name="datadocumento" id="datadocumento" value="${despesa.dataDocumento}"/>                                
                            </div>
                            <div class="col-sm">
                                <label>Valor da Despesa</label>
                                <input class="form-control" type="text" style="text-align: right;" name="valordespesa" id="valordespesa" value="<fmt:formatNumber value='${despesa.valorDespesa}' type='currency'/>"/>
                            </div>
                            <div class="col-sm">
                                <label>Valor Pago</label>
                                <input class="form-control" type="text" style="text-align: right;" name="valorpago" id="valorpago" value="<fmt:formatNumber value='${despesa.valorPago}' type='currency'/>"/>
                            </div>
                        </div>
                    </div>
                    <div class="form-group">
                        <button class="btn btn-success" type="submit" id="submit" onclick="validarCampos()">Salvar Documento</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<style type="text/css">
    .inputfile {
        width: 0.1px;
        height: 0.1px;
        opacity: 0;
        overflow: hidden;
        position: absolute;
        z-index: -1;
    }
    .inputfile:focus + label{
        outline: 1px dotted #000;
        outline: -webkit-focus-ring-color auto 5px;
    }
    .inputfile + label *{
        pointer-events: none;
    }
    .borda{
        position: relative;
        margin: 0 20px 30px 0;
        padding: 10px;
        border: 1px solid #e1e1e1;
        border-radius: 3px;
        background: #fff;
        -webkit-box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
        -moz-box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
        box-shadow: 0px 0px 3px rgba(0,0,0,0.06);
    }
</style>

<script>
    function gravarDados() {
        console.log("Gravando dados ...");
        var target = document.getElementById("target").src;
        $.ajax({
            type: 'post',
            url: 'DespesaCadastrar',
            data: {
                iddespesa: $('#iddespesa').val(),
                descricao: $('#descricao').val(),
                datadocumento: $('#datadocumento').val(),
                valordespesa: $('#valordespesa').val(),
                valorpago: $('#valorpago').val(),
                imagemdocumento: target
            },
            success:
                    function (data) {
                        console.log("resposta servlet->");
                        console.log(data);
                        if (data == 1) {
                            Swal.fire({
                                position: 'center',
                                icon: 'success',
                                title: 'Sucesso',
                                text: 'Despesa gravada com sucesso!',
                                showConfirmaButton: false,
                                timer: 1000
                            })
                        } else {
                            Swal.fire({
                                position: 'center',
                                icon: 'error',
                                title: 'Erro',
                                text: 'Não foi possivel gravar a despesa!',
                                showConfirmaButton: false,
                                timer: 1000
                            })
                        }
                        window.location.href = "${pageContext.request.contextPath}/DespesaListar";
                    },
            error:
                    function (data) {
                        window.location.href = "${pageContext.request.contextPath}/DespesaListar";
                    }
        });
    }

    function validarCampos() {
        console.log("entrei na validação de campos");
        if (document.getElementById("descricao").value == '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique a descrição da despesa!',
                showConfirmaButton: false,
                timer: 1000
            });
            $("#descricao").focus();
        } else if (document.getElementById("datadocumento").value == '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique a Data da despesa!',
                showConfirmaButton: false,
                timer: 1000
            });
            $("#datadocumento").focus();
        } else if (document.getElementById("valordespesa").value == '') {
            Swal.fire({
                position: 'center',
                icon: 'error',
                title: 'Verifique a Valor da despesa!',
                showConfirmaButton: false,
                timer: 1000
            });
            $("#valordespesa").focus();
        } else {
            gravarDados();
        }
    }

    $(document).ready(function () {
        console.log("entrei na ready do documento");
        $("#valordespesa").maskMoney({
            prefix: 'R$',
            suffix: '',
            allowZero: false,
            allowNegative: true,
            allowEmpty: false,
            doubleClickSelection: true,
            selectAllOnFocus: true,
            thousands: '.',
            decimal: ",",
            precision: 2,
            affixesStay: true,
            bringCareAtEndOnFocus: true
        })

        $("#valorpago").maskMoney({
            prefix: 'R$',
            suffix: '',
            allowZero: false,
            allowNegative: true,
            allowEmpty: false,
            doubleClickSelection: true,
            selectAllOnFocus: true,
            thousands: '.',
            decimal: ",",
            precision: 2,
            affixesStay: true,
            bringCareAtEndOnFocus: true
        })
    })

    function uploadFile() {
        var target = document.getElementById("target");
        target.src = "";
        var file = document.querySelector("input[type = 'file']").files[0];
        if (file) {
            var reader = new FileReader();
            reader.readAsDataURL(file);
            reader.onloadend = function () {
                target.src = reader.result;
            };
        } else {
            target.src = "";
        }
    }
</script>

<%@include file="/footer.jsp"%>