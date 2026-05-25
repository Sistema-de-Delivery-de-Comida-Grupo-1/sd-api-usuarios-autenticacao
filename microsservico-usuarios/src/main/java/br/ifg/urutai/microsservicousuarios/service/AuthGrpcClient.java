package br.ifg.urutai.microsservicousuarios.service;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class AuthGrpcClient {

    @GrpcClient("microsservico-autenticacao")
    private br.ifg.urutai.microsservicousuarios.grpc.AuthServiceGrpc.AuthServiceBlockingStub authStub;

    public String autenticar(String email, String senha) {
        // Monta o objeto que será enviado pela rede
        br.ifg.urutai.microsservicousuarios.grpc.LoginRequest request = br.ifg.urutai.microsservicousuarios.grpc.LoginRequest.newBuilder()
                .setEmail(email)
                .setSenha(senha)
                .build();

        try {
            br.ifg.urutai.microsservicousuarios.grpc.LoginResponse response = authStub.validarLogin(request);

            if (response.getSucesso()) {
                return response.getToken();
            } else {
                throw new RuntimeException("Falha na autenticação: " + response.getMensagem());
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao comunicar com o servidor de autenticação: " + e.getMessage());
        }
    }
}